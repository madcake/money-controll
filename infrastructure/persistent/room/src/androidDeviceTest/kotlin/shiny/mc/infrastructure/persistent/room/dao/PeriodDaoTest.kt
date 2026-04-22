package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import shiny.mc.infrastructure.persistent.room.RoomStore
import shiny.mc.infrastructure.persistent.room.entity.CategoryEntity
import shiny.mc.infrastructure.persistent.room.entity.CategoryTypeEntity
import shiny.mc.infrastructure.persistent.room.entity.RecordEntity
import shiny.mc.infrastructure.persistent.room.entity.TransactionEntity

@RunWith(AndroidJUnit4::class)
class PeriodDaoTest {

    private lateinit var database: RoomStore
    private lateinit var categoryDao: CategoryDao
    private lateinit var recordDao: RecordDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var periodDao: PeriodDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomStore::class.java
        ).allowMainThreadQueries().build()
        categoryDao = database.categoryDao()
        recordDao = database.categoryRecordDao()
        transactionDao = database.transactionDao()
        periodDao = database.periodDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getPeriods_summarizesDataCorrectly() = runBlocking {
        // Prepare categories
        val incomeCategoryId = categoryDao.insert(CategoryEntity(title = "Salary", type = CategoryTypeEntity.In))
        val expenseCategoryId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))

        // Prepare records for Jan 2024
        val record1 = RecordEntity(id = "1", categoryId = incomeCategoryId, month = 1, year = 2024, estimateValue = 5000.0)
        val record2 = RecordEntity(id = "2", categoryId = expenseCategoryId, month = 1, year = 2024, estimateValue = 1000.0)
        recordDao.insert(listOf(record1, record2))

        // Prepare transactions for Jan 2024
        transactionDao.insert(TransactionEntity(id = 0, recordId = "1", value = 5000.0, purpose = "Jan Salary", datetime = 0))
        transactionDao.insert(TransactionEntity(id = 0, recordId = "2", value = 300.0, purpose = "Grocery", datetime = 0))
        transactionDao.insert(TransactionEntity(id = 0, recordId = "2", value = 200.0, purpose = "Dinner", datetime = 0))

        val periods = periodDao.getPeriods().first()
        assertEquals(1, periods.size)
        val jan = periods[0]
        assertEquals(1, jan.month)
        assertEquals(2024, jan.year)
        assertEquals(5000.0, jan.inEstimate, 0.0)
        assertEquals(5000.0, jan.inReal, 0.0)
        assertEquals(1000.0, jan.outEstimate, 0.0)
        assertEquals(500.0, jan.outReal, 0.0)
    }

    @Test
    fun getPeriod_returnsSpecificPeriod() = runBlocking {
        val catId = categoryDao.insert(CategoryEntity(title = "Salary", type = CategoryTypeEntity.In))
        recordDao.insert(RecordEntity(id = "1", categoryId = catId, month = 1, year = 2024, estimateValue = 5000.0))
        recordDao.insert(RecordEntity(id = "2", categoryId = catId, month = 2, year = 2024, estimateValue = 6000.0))

        val jan = periodDao.getPeriod(1, 2024).first()
        assertNotNull(jan)
        assertEquals(5000.0, jan!!.inEstimate, 0.0)

        val feb = periodDao.getPeriod(2, 2024).first()
        assertNotNull(feb)
        assertEquals(6000.0, feb!!.inEstimate, 0.0)
    }

    @Test
    fun copy_duplicatesRecordsToNewPeriod() = runBlocking {
        val catId = categoryDao.insert(CategoryEntity(title = "Food", type = CategoryTypeEntity.Out))
        recordDao.insert(RecordEntity(id = "food:1:2024", categoryId = catId, month = 1, year = 2024, estimateValue = 1000.0))

        periodDao.copy(fromMonth = 1, fromYear = 2024, toMonth = 2, toYear = 2024)

        val febPeriod = periodDao.getPeriod(2, 2024).first()
        assertNotNull(febPeriod)
        assertEquals(1000.0, febPeriod!!.outEstimate, 0.0)
        
        val febRecords = recordDao.getRecords(2, 2024).first()
        assertEquals(1, febRecords.size)
        assertEquals("1:2:2024", febRecords[0].id)
    }
}
