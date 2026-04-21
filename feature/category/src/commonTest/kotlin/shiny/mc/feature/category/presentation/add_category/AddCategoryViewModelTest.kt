package shiny.mc.feature.category.presentation.add_category

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import shiny.mc.core.domain.value.CategoryValue
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import shiny.mc.core.ports.category.AddCategory
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.model.processCommand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddCategoryViewModelTest {

    private class MockAddCategory : AddCategory {
        var stubbedResult: Boolean = true
        var stubbedError: Throwable? = null

        override suspend fun addCategory(title: String, type: CategoryType): Boolean {
            stubbedError?.let { throw it }
            return stubbedResult
        }
    }

    @Test
    fun processCommand_emitsSuccess_whenAddCategorySucceeds() = runTest {
        val mockAddCategory = MockAddCategory()
        val categoryValue = CategoryValue("Salary", CategoryType.In)
        val command = Command.Action(categoryValue)

        val results = flowOf(command).processCommand {
            mockAddCategory.addCategory(it.title, it.type)
        }.toList()

        assertEquals(1, results.size)
        assertTrue(results[0] is CommandState.Success)
        assertEquals(command, (results[0] as CommandState.Success).command)
    }

    @Test
    fun processCommand_emitsFailure_whenAddCategoryThrows() = runTest {
        val mockAddCategory = MockAddCategory().apply {
            stubbedError = CategoryError.EmptyTitle()
        }
        val command = Command.Action(CategoryValue("", CategoryType.Out))

        val results = flowOf(command).processCommand {
            mockAddCategory.addCategory(it.title, it.type)
        }.toList()

        assertTrue(results[0] is CommandState.Failure)
        assertEquals(mockAddCategory.stubbedError, (results[0] as CommandState.Failure).err)
    }

    @Test
    fun processCommand_emitsIdle_onReset() = runTest {
        val results = flowOf(Command.Reset<CategoryValue>()).processCommand {
            // Not called
        }.toList()

        assertTrue(results[0] is CommandState.Idle)
    }
}
