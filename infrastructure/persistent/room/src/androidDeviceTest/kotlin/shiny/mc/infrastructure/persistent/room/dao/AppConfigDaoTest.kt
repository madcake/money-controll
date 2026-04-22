package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import shiny.mc.infrastructure.persistent.room.RoomStore
import shiny.mc.infrastructure.persistent.room.entity.AppConfigEntity
import shiny.mc.infrastructure.persistent.room.entity.AppConfigKeyEntity

@RunWith(AndroidJUnit4::class)
class AppConfigDaoTest {

    private lateinit var database: RoomStore
    private lateinit var appConfigDao: AppConfigDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomStore::class.java
        ).allowMainThreadQueries().build()
        appConfigDao = database.appConfigDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetConfig() = runBlocking {
        val entity = AppConfigEntity(
            AppConfigKeyEntity.CurrentPeriod,
            "10-2026"
        )

        appConfigDao.insert(entity)
        val result = appConfigDao.get(AppConfigKeyEntity.CurrentPeriod).first()
        assertEquals("10-2026", result)
    }

    @Test
    fun getNonExistentConfigReturnsNull() = runBlocking {
        val key = AppConfigKeyEntity.CurrentPeriod
        val result = appConfigDao.get(key).first()

        assertNull(result)
    }

    @Test
    fun updateExistingConfig() = runBlocking {
        val key = AppConfigKeyEntity.CurrentPeriod
        val value1 = "2023-10"
        val value2 = "2023-11"

        appConfigDao.insert(AppConfigEntity(key, value1))
        appConfigDao.insert(AppConfigEntity(key, value2))

        val result = appConfigDao.get(key).first()
        assertEquals(value2, result)
    }
}
