package shiny.mc.feature.add_category

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.category.AddCategory
import shiny.mc.core.coordinators.category.CategoryInputValidator
import shiny.mc.core.domain.value.CategoryError
import shiny.mc.core.domain.value.CategoryType

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AddCategoryViewModel(
    private val addCategory: AddCategory,
    private val categoryInputValidator: CategoryInputValidator
): ViewModel() {

    val titleState = TextFieldState()
    private val title = snapshotFlow { titleState.text.toString() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val type: StateFlow<CategoryType>
        field = MutableStateFlow(CategoryType.Liability)

    val inputValid = combine(
        title,
        type,
    ) { title, type ->
        try {
            categoryInputValidator.validateCategoryInput(title, type)
        } catch (_: CategoryError) {
            false
        } catch (_: Throwable) {
            false
        }
    }
    .flowOn(Dispatchers.IO)
    .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val command = MutableStateFlow<AddCategoryCmd>(AddCategoryCmd.None)

    val commandState = combine(
        title.onEach { command.update { AddCategoryCmd.None } },
        type,
        command,
    ) { title, type, command ->
        Triple(title, type, command)
    }.filter { it.third != AddCategoryCmd.None }.flatMapLatest {
        val (title, type, command) = it
        flow {
            emit(CommandState.Processing(command))
            try {
                categoryInputValidator.validateCategoryInput(it.first, it.second)

                if (addCategory.addCategory(it.first, it.second)) {
                    emit(CommandState.Success(command))
                } else {
                    throw CategoryError.UnknownError("Save category error")
                }
            } catch (err: CategoryError) {
                emit(CommandState.Failure(err, command))
            } catch (err: Throwable) {
                emit(
                    CommandState.Failure(
                        CategoryError.UnknownError(err.message ?: err.stackTraceToString()),
                        command
                    )
                )
            }
        }
    }.onEach { state ->
        when (state) {
            is CommandState.Failure,
            is CommandState.Success -> command.update { AddCategoryCmd.None } // Reset command
            CommandState.Idle,
            is CommandState.Processing -> {}
        }
    }
    .onEach {
        if (it is CommandState.Success) {
            titleState.clearText()
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), CommandState.Idle)

    fun onCategoryTypeSelected(type: CategoryType) {
        this.type.update { type }
    }

    fun onSave() {
        command.update { AddCategoryCmd.Save() }
    }
}

sealed interface AddCategoryCmd {
    object None : AddCategoryCmd
    class Save : AddCategoryCmd
}

sealed class CommandState<Command>(val command: Command? = null) {
    object Idle : CommandState<AddCategoryCmd>()
    class Processing<Command>(command: Command) : CommandState<Command>(command)
    class Success<Command>(command: Command) : CommandState<Command>(command)
    class Failure<Command>(val err: Throwable, command: Command) : CommandState<Command>(command)
}