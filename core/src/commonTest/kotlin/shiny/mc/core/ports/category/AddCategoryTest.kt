package shiny.mc.core.ports.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import shiny.mc.core.domain.CommandState
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddCategoryTest {

    private class TestAddCategory(
        val onAddCategory: suspend (String, CategoryType) -> Boolean
    ) : AddCategory {
        override suspend fun addCategory(title: String, type: CategoryType): Boolean {
            return onAddCategory(title, type)
        }
    }

    @Test
    fun add_skipsNoneCommand() = runTest {
        val testAddCategory = TestAddCategory { _, _ -> true }
        val input = flowOf(
            Triple("Test", CategoryType.In, AddCategoryCommand.None)
        )

        val results = testAddCategory.add(input).toList()

        assertTrue(results.isEmpty(), "Should skip None command")
    }

    @Test
    fun add_emitsProcessingAndSuccess_whenSaveSucceeds() = runTest {
        val command = AddCategoryCommand.Save()
        val testAddCategory = TestAddCategory { title, type ->
            assertEquals("Salary", title)
            assertEquals(CategoryType.In, type)
            true
        }
        val input = flowOf(
            Triple("Salary", CategoryType.In, command)
        )

        val results = testAddCategory.add(input).toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is CommandState.Processing)
        assertEquals(command, (results[0] as CommandState.Processing).command)
        assertTrue(results[1] is CommandState.Success)
        assertEquals(command, (results[1] as CommandState.Success).command)
    }

    @Test
    fun add_emitsProcessingAndFailure_whenSaveReturnsFalse() = runTest {
        val command = AddCategoryCommand.Save()
        val testAddCategory = TestAddCategory { _, _ -> false }
        val input = flowOf(
            Triple("Fail", CategoryType.Out, command)
        )

        val results = testAddCategory.add(input).toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is CommandState.Processing)
        assertTrue(results[1] is CommandState.Failure)
        val failure = results[1] as CommandState.Failure
        assertEquals(command, failure.command)
        assertTrue(failure.err is CategoryError.UnknownError)
        assertEquals("Save category error", failure.err.message)
    }

    @Test
    fun add_emitsProcessingAndFailure_whenAddCategoryThrowsCategoryError() = runTest {
        val command = AddCategoryCommand.Save()
        val error = CategoryError.EmptyTitle()
        val testAddCategory = TestAddCategory { _, _ -> throw error }
        val input = flowOf(
            Triple("", CategoryType.Out, command)
        )

        val results = testAddCategory.add(input).toList()

        assertEquals(2, results.size)
        assertTrue(results[1] is CommandState.Failure)
        assertEquals(error, (results[1] as CommandState.Failure).err)
    }

    @Test
    fun add_emitsProcessingAndFailure_whenAddCategoryThrowsGenericException() = runTest {
        val command = AddCategoryCommand.Save()
        val testAddCategory = TestAddCategory { _, _ -> throw RuntimeException("Boom") }
        val input = flowOf(
            Triple("Boom", CategoryType.Out, command)
        )

        val results = testAddCategory.add(input).toList()

        assertEquals(2, results.size)
        assertTrue(results[1] is CommandState.Failure)
        val failure = results[1] as CommandState.Failure
        assertTrue(failure.err is CategoryError.UnknownError)
        assertEquals(failure.err.message?.contains("Boom"), true)
    }

    @Test
    fun onSuccess_executesAction_onSuccessState() = runTest {
        val command = AddCategoryCommand.Save()
        val flow: Flow<CommandState<AddCategoryCommand>> = flowOf(
            CommandState.Success(command)
        )
        var called = false

        flow.onSuccess {
            called = true
            assertEquals(command, it.command)
        }.toList()

        assertTrue(called, "Action should be called on Success")
    }

    @Test
    fun onFailure_executesAction_onFailureState() = runTest {
        val command = AddCategoryCommand.Save()
        val flow: Flow<CommandState<AddCategoryCommand>> = flowOf(
            CommandState.Failure(CategoryError.EmptyTitle(), command)
        )
        var called = false

        flow.onFailure {
            called = true
            assertEquals(command, it.command)
        }.toList()

        assertTrue(called, "Action should be called on Failure")
    }

    @Test
    fun onReset_executesAction_onSuccessAndFailure() = runTest {
        val command = AddCategoryCommand.Save()
        val flow: Flow<CommandState<AddCategoryCommand>> = flowOf(
            CommandState.Success(command),
            CommandState.Failure(CategoryError.EmptyTitle(), command),
            CommandState.Processing(command)
        )
        var callCount = 0

        flow.onReset {
            callCount++
        }.toList()

        assertEquals(2, callCount, "Action should be called exactly twice (Success and Failure)")
    }
}
