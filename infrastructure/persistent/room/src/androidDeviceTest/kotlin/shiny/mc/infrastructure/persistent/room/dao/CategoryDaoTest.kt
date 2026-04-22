package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import shiny.mc.infrastructure.persistent.room.RoomStore
import shiny.mc.infrastructure.persistent.room.entity.CategoryEntity
import shiny.mc.infrastructure.persistent.room.entity.CategoryTypeEntity

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var database: RoomStore
    private lateinit var categoryDao: CategoryDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoomStore::class.java
        ).allowMainThreadQueries().build()
        categoryDao = database.categoryDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetCategoryById() = runBlocking {
        val category = CategoryEntity(title = "Food", type = CategoryTypeEntity.Out)
        val id = categoryDao.insert(category)
        
        val result = categoryDao.getCategory(id).first()
        
        assertEquals("Food", result?.title)
        assertEquals(CategoryTypeEntity.Out, result?.type)
        assertEquals(id, result?.id)
    }

    @Test
    fun insertAndGetCategoryByTitle() = runBlocking {
        val category1 = CategoryEntity(title = "Salary", type = CategoryTypeEntity.In)
        val category2 = CategoryEntity(title = "Salary1", type = CategoryTypeEntity.In)
        categoryDao.insert(category1)
        categoryDao.insert(category2)

        val result = categoryDao.getCategory("Salary").first()
        
        assertEquals("Salary", result?.title)
        assertEquals(CategoryTypeEntity.In, result?.type)
    }

    @Test
    fun getAllCategories() = runBlocking {
        val c1 = CategoryEntity(title = "A", type = CategoryTypeEntity.In)
        val c2 = CategoryEntity(title = "B", type = CategoryTypeEntity.Out)
        
        categoryDao.insert(c1)
        categoryDao.insert(c2)
        
        val result = categoryDao.getCategories().first()
        
        assertEquals(2, result.size)
        assertTrue(result.any { it.title == "A" })
        assertTrue(result.any { it.title == "B" })
    }

    @Test
    fun updateCategory() = runBlocking {
        val category = CategoryEntity(title = "Food", type = CategoryTypeEntity.Out)
        val id = categoryDao.insert(category)
        
        val updatedCategory = CategoryEntity(id = id, title = "Groceries", type = CategoryTypeEntity.Out)
        categoryDao.update(updatedCategory)
        
        val result = categoryDao.getCategory(id).first()
        assertEquals("Groceries", result?.title)
    }

    @Test
    fun deleteCategory() = runBlocking {
        val category = CategoryEntity(title = "Food", type = CategoryTypeEntity.Out)
        val id = categoryDao.insert(category)
        
        categoryDao.delete(id)
        
        val result = categoryDao.getCategory(id).first()
        assertNull(result)
    }

    @Test
    fun findCategoriesByTitle() = runBlocking {
        categoryDao.insert(CategoryEntity(title = "Health", type = CategoryTypeEntity.Out))
        categoryDao.insert(CategoryEntity(title = "Wealth", type = CategoryTypeEntity.In))
        categoryDao.insert(CategoryEntity(title = "Entertainment", type = CategoryTypeEntity.Out))
        
        val result = categoryDao.find("ealth").first()
        
        assertEquals(2, result.size)
        assertTrue(result.any { it.title == "Health" })
        assertTrue(result.any { it.title == "Wealth" })
    }
}
