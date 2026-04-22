package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
class RecordDaoTest {

    private lateinit var database: RoomStore
    private lateinit var recordDao: RecordDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao

    private var categoryId: Long = 0

    @Before
    fun createDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomStore::class.java
        ).allowMainThreadQueries().build()
        recordDao = database.categoryRecordDao()
        categoryDao = database.categoryDao()
        transactionDao = database.transactionDao()

        categoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetRecord() = runBlocking {
        val record = RecordEntity(
            id = "record1",
            categoryId = categoryId,
            month = 5,
            year = 2023,
            estimateValue = 100.0
        )
        recordDao.insert(record)

        val result = recordDao.getRecord("record1").first()
        assertNotNull(result)
        assertEquals("record1", result?.id)
        assertEquals(categoryId, result?.categoryId)
        assertEquals(5, result?.month)
        assertEquals(2023, result?.year)
        assertEquals(100.0, result!!.estimateValue, 0.0)
        assertEquals("Food", result.title)
        assertEquals(CategoryTypeEntity.Out, result.type)
        assertNull(result.real) // No transactions yet
    }

    @Test
    fun insertMultipleAndGetAll() = runBlocking {
        val records = listOf(
            RecordEntity("r1", categoryId, 1, 2023, 10.0),
            RecordEntity("r2", categoryId, 2, 2023, 20.0)
        )
        recordDao.insert(records)

        val result = recordDao.getRecords().first()
        assertEquals(2, result.size)
        assertEquals("r1", result[0].id)
        assertEquals("r2", result[1].id)
    }

    @Test
    fun updateRecord() = runBlocking {
        val record = RecordEntity("r1", categoryId, 1, 2023, 10.0)
        recordDao.insert(record)

        val updated = RecordEntity("r1", categoryId, 1, 2023, 15.0)
        recordDao.update(updated)

        val result = recordDao.getRecord("r1").first()
        assertEquals(15.0, result!!.estimateValue, 0.0)
    }

    @Test
    fun deleteRecord() = runBlocking {
        val record = RecordEntity("r1", categoryId, 1, 2023, 10.0)
        recordDao.insert(record)
        assertTrue(recordDao.hasRecord("r1"))

        recordDao.delete("r1")
        assertFalse(recordDao.hasRecord("r1"))
    }

    @Test
    fun getRecordsByMonthAndYear() = runBlocking {
        recordDao.insert(RecordEntity("r1", categoryId, 5, 2023, 10.0))
        recordDao.insert(RecordEntity("r2", categoryId, 6, 2023, 20.0))

        val result = recordDao.getRecords(5, 2023).first()
        assertEquals(1, result.size)
        assertEquals("r1", result[0].id)
    }

    @Test
    fun getRecordsByCategoryId() = runBlocking {
        val otherCategoryId = categoryDao.insert(CategoryEntity(title = "Rent", type = CategoryTypeEntity.Out))
        recordDao.insert(RecordEntity("r1", categoryId, 5, 2023, 10.0))
        recordDao.insert(RecordEntity("r3", categoryId, 5, 2023, 14.0))
        recordDao.insert(RecordEntity("r2", otherCategoryId, 5, 2023, 20.0))

        val result = recordDao.getRecords(categoryId).first()
        assertEquals(2, result.size)
        assertEquals("r1", result[0].id)
        assertEquals("r3", result[1].id)
    }

    @Test
    fun testRealValueSumming() = runBlocking {
        val recordId = "record_with_tx"
        recordDao.insert(RecordEntity(recordId, categoryId, 5, 2023, 100.0))

        transactionDao.insert(TransactionEntity(0, recordId, 10.5, "Lunch", 123456789L))
        transactionDao.insert(TransactionEntity(0, recordId, 20.0, "Dinner", 123456790L))

        val result = recordDao.getRecord(recordId).first()
        assertNotNull(result)
        assertEquals(30.5, result?.real ?: 0.0, 0.0)
    }
}
