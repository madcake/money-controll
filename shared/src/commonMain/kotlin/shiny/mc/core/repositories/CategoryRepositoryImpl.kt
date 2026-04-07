package shiny.mc.core.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Singleton
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.entity.Category
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.RecordDao
import shiny.mc.services.store.entity.toDto
import shiny.mc.services.store.entity.toEntity

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
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

    override suspend fun addRecord(record: Record) {
        addRecords(listOf(record))
    }

    override suspend fun addRecords(records: List<Record>) {
        recordDao.insert(records.toEntity())
    }

    override fun getRecords(): Flow<List<Record>> {
        TODO("Not yet implemented")
    }

    override fun getRecord(recordId: String): Flow<Record?> {
        return recordDao.getRecord(recordId).map { it?.toDto() }
    }

    override fun getRecords(categoryId: Long): Flow<List<Record>> {
        TODO("Not yet implemented")
    }

    override fun getRecords(
        month: Int,
        year: Int,
    ): Flow<List<Record>> {
        return recordDao.getRecords(month, year).toDto()
    }

    override suspend fun updateRecord(record: Record) {
        recordDao.update(record.toEntity())
    }
}