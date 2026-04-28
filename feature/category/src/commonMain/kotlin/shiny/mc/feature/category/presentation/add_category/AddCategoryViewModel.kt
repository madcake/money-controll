package shiny.mc.feature.category.presentation.add_category

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.domain.value.CategoryValue
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.ports.category.AddCategory
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.model.onSuccess
import shiny.mc.core_ui.model.processCommand

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

    private val command = MutableSharedFlow<Command<CategoryValue>>()

    val commandState =
        command.processCommand {
            addCategory.addCategory(it.title, it.type)
        }
        .onSuccess { titleState.clearText() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(500),
            CommandState.Idle()
        )

    fun onCategoryTypeSelected(type: CategoryType) {
        this.type.update { type }
    }

    fun onSave() = viewModelScope.launch {
        command.emit(Command.Action(CategoryValue(title.value, type.value)))
    }
}