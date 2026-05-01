package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import shiny.mc.infrastructure.persistent.room.RoomStore
import shiny.mc.infrastructure.persistent.room.entity.CategoryEntity
import shiny.mc.infrastructure.persistent.room.entity.CategoryTypeEntity
import shiny.mc.infrastructure.persistent.room.entity.RecordEntity
import shiny.mc.infrastructure.persistent.room.entity.TransactionEntity

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {

    private lateinit var database: RoomStore
    private lateinit var categoryDao: CategoryDao
    private lateinit var recordDao: RecordDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomStore::class.java
        ).allowMainThreadQueries().build()
        categoryDao = database.categoryDao()
        recordDao = database.categoryRecordDao()
        transactionDao = database.transactionDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetTransactions() = runBlocking {
        // Setup parent entities
        val categoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        val recordId = "record1"
        recordDao.insert(RecordEntity(id = recordId, categoryId = categoryId, month = 1, year = 2024, estimateValue = 100.0))

        val tx = TransactionEntity(id = 0, recordId = recordId, value = 10.5, purpose = "Lunch", datetime = 123456789L)
        val txId = transactionDao.insert(tx)

        val result = transactionDao.getTransactions(recordId).first()

        assertEquals(1, result.size)
        assertEquals(txId, result[0].id)
        assertEquals("Lunch", result[0].purpose)
        assertEquals(10.5, result[0].value, 0.0)
    }

    @Test
    fun deleteTransaction() = runBlocking {
        val categoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        val recordId = "record1"
        recordDao.insert(RecordEntity(id = recordId, categoryId = categoryId, month = 1, year = 2024, estimateValue = 100.0))

        val txId = transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 10.5, purpose = "Lunch", datetime = 123456789L))
        
        transactionDao.delete(txId)
        
        val result = transactionDao.getTransactions(recordId).first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getSuggestions() = runBlocking {
        val categoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        val recordId = "record1"
        recordDao.insert(RecordEntity(id = recordId, categoryId = categoryId, month = 1, year = 2024, estimateValue = 100.0))

        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 10.0, purpose = "Burger King", datetime = 1L))
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 15.0, purpose = "Burger Queen", datetime = 2L))
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 5.0, purpose = "Coffee", datetime = 3L))

        val suggestions = transactionDao.getSuggestions(categoryId, "Burger").first()
        
        assertEquals(2, suggestions.size)
        assertTrue(suggestions.contains("Burger King"))
        assertTrue(suggestions.contains("Burger Queen"))
    }

    @Test
    fun getSuggestions_differentCategory() = runBlocking {
        val categoryId1 = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        val categoryId2 = categoryDao.insert(CategoryEntity(title = "Transport", type = CategoryTypeEntity.Out))
        
        val recordId1 = "record1"
        val recordId2 = "record2"
        
        recordDao.insert(RecordEntity(id = recordId1, categoryId = categoryId1, month = 1, year = 2024, estimateValue = 100.0))
        recordDao.insert(RecordEntity(id = recordId2, categoryId = categoryId2, month = 1, year = 2024, estimateValue = 50.0))

        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId1, value = 10.0, purpose = "Burger King", datetime = 1L))
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId2, value = 15.0, purpose = "Bus", datetime = 2L))

        val foodSuggestions = transactionDao.getSuggestions(categoryId1, "").first()
        assertEquals(1, foodSuggestions.size)
        assertEquals("Burger King", foodSuggestions[0])
        
        val transportSuggestions = transactionDao.getSuggestions(categoryId2, "").first()
        assertEquals(1, transportSuggestions.size)
        assertEquals("Bus", transportSuggestions[0])
    }

    @Test
    fun getSuggestions_uniqueAndOrdered() = runBlocking {
        val categoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        val recordId = "record1"
        recordDao.insert(RecordEntity(id = recordId, categoryId = categoryId, month = 1, year = 2024, estimateValue = 100.0))

        // Insert duplicate purposes
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 10.0, purpose = "Coffee", datetime = 1L))
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 5.0, purpose = "Coffee", datetime = 2L))
        
        // Insert purposes that should be ordered alphabetically
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 15.0, purpose = "Apple", datetime = 3L))
        transactionDao.insert(TransactionEntity(id = 0, recordId = recordId, value = 20.0, purpose = "Banana", datetime = 4L))

        val suggestions = transactionDao.getSuggestions(categoryId, "").first()
        
        assertEquals(3, suggestions.size)
        assertEquals("Apple", suggestions[0])
        assertEquals("Banana", suggestions[1])
        assertEquals("Coffee", suggestions[2])
    }
}
