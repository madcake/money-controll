package shiny.mc.feature.category.ports

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AddCategoryImplTest {

    private val categoryRepository = MockCategoryRepository()
    private val addCategory = AddCategoryImpl(categoryRepository)

    @Test
    fun addCategory_succeeds_whenTitleIsValidAndNotDuplicated() = runTest {
        val result = addCategory.addCategory("Food", CategoryType.Out)
        assertTrue(result)
        assertTrue(categoryRepository.addCategoryCalled)
    }

    @Test
    fun addCategory_throwsEmptyTitle_whenTitleIsEmpty() = runTest {
        assertFailsWith<CategoryError.EmptyTitle> {
            addCategory.addCategory("", CategoryType.Out)
        }
    }

    @Test
    fun addCategory_throwsDuplicatedTitle_whenTitleAlreadyExists() = runTest {
        categoryRepository.stubbedCategory = Category(id = 1, title = "Food", type = CategoryType.Out)
        
        assertFailsWith<CategoryError.DuplicatedTitle> {
            addCategory.addCategory("Food", CategoryType.Out)
        }
    }

    private class MockCategoryRepository : CategoryRepository {
        var addCategoryCalled = false
        var stubbedCategory: Category? = null

        override fun getCategories(): Flow<List<Category>> = flowOf(emptyList())

        override suspend fun addCategory(category: Category): Category {
            addCategoryCalled = true
            return category
        }

        override suspend fun deleteCategory(id: Long) {}

        override suspend fun updateCategory(category: Category) {}

        override fun getCategory(id: Long): Flow<Category?> = flowOf(null)

        override fun getCategory(title: String): Flow<Category?> = flowOf(stubbedCategory)

        override fun find(query: String): Flow<List<Category>> = flowOf(emptyList())
    }
}
