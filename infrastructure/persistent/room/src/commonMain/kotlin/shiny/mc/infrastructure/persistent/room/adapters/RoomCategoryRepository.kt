package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.dto.Category
import shiny.mc.infrastructure.persistent.room.dao.CategoryDao
import shiny.mc.infrastructure.persistent.room.entity.toDto
import shiny.mc.infrastructure.persistent.room.entity.toEntity

@OptIn(ExperimentalCoroutinesApi::class)
class RoomCategoryRepository(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().toDto()
    }

    override suspend fun addCategory(category: Category): Category {
        val id = categoryDao.insert(category.toEntity())
        return category.copy(id = id)
    }

    override suspend fun deleteCategory(id: Long) {
        categoryDao.delete(id)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override fun getCategory(id: Long): Flow<Category?> {
        return categoryDao.getCategory(id).mapLatest { it?.toDto() }
    }

    override fun getCategory(title: String): Flow<Category?> {
        return categoryDao.getCategory(title).mapLatest { it?.toDto() }
    }

    override fun find(query: String): Flow<List<Category>> {
        return categoryDao.find(query).toDto()
    }
}