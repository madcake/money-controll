package shiny.mc.feature.category.presentation.add_category

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.domain.CommandState
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import shiny.mc.core.ports.category.AddCategory
import shiny.mc.feature.category.model.AddCategoryCommand

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AddCategoryViewModel(
    private val addCategory: AddCategory,
): ViewModel() {

    val titleState = TextFieldState()
    private val title = snapshotFlow { titleState.text.toString() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val type: StateFlow<CategoryType>
        field = MutableStateFlow(CategoryType.Out)

    private val command = MutableStateFlow<AddCategoryCommand>(AddCategoryCommand.None)

    val commandState = combine(
        title.onEach { command.update { AddCategoryCommand.None } },
        type,
        command,
    ) { title, type, command ->
        Triple(title, type, command)
    }.filter { it.third != AddCategoryCommand.None }.flatMapLatest {
        val (title, type, command) = it
        flow {
            emit(CommandState.Processing(command))
            try {
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
            is CommandState.Success -> command.update { AddCategoryCommand.None } // Reset command
            is CommandState.Idle,
            is CommandState.Processing -> {}
        }
    }
    .onEach {
        if (it is CommandState.Success) {
            titleState.clearText()
        }
    }
    .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500),
        CommandState.Idle()
    )

    fun onCategoryTypeSelected(type: CategoryType) {
        this.type.update { type }
    }

    fun onSave() {
        command.update { AddCategoryCommand.Save() }
    }
}