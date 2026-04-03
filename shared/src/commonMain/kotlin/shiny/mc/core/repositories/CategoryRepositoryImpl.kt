package shiny.mc.core.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Singleton
import shiny.mc.core.domain.aggregate.CategoryRecord
import shiny.mc.core.domain.entity.Category
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.CategoryRecordDao
import shiny.mc.services.store.entity.toDto
import shiny.mc.services.store.entity.toEntity

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val record: CategoryRecordDao,
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

    override suspend fun addRecord(record: CategoryRecord) {
        TODO("Not yet implemented")
    }

    override fun getRecords(): Flow<List<CategoryRecord>> {
        TODO("Not yet implemented")
    }

    override fun getRecord(recordId: String): Flow<CategoryRecord?> {
        TODO("Not yet implemented")
    }

    override fun getRecords(categoryId: Long): Flow<List<CategoryRecord>> {
        TODO("Not yet implemented")
    }

    override fun getRecords(
        start: Long,
        end: Long
    ): Flow<List<CategoryRecord>> {
        TODO("Not yet implemented")
    }
}