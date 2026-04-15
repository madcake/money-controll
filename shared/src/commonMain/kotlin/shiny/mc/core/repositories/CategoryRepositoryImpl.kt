package shiny.mc.core.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.RecordDao
import shiny.mc.services.store.entity.toDto
import shiny.mc.services.store.entity.toEntity

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val recordDao: RecordDao,
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