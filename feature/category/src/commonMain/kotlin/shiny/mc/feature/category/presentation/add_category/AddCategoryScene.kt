package shiny.mc.feature.category.presentation.add_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import shiny.mc.core_ui.components.SmallCircularProgressIndicator
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.resources.Res
import shiny.mc.core_ui.resources.error_category_duplicated_title
import shiny.mc.core_ui.resources.error_category_empty_title
import shiny.mc.core_ui.resources.placeholders_category_title
import shiny.mc.core_ui.theme.space

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun AddCategoryScene(
    title: TextFieldState,
    categoryType: CategoryType,
    commandState: CommandState<Command>,
    onCategoryTypeSelected: (CategoryType) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = title,
            placeholder = { Text(stringResource(Res.string.placeholders_category_title)) },
            enabled = commandState !is CommandState.Processing,
            lineLimits = TextFieldLineLimits.SingleLine,
            leadingIcon = {
                CategoryTypeMenu(
                    categoryType = categoryType,
                    commandState = commandState,
                    onCategoryTypeSelected = onCategoryTypeSelected,
                )
            },
            trailingIcon = {
                IconButton(onClick = onSave) {
                    when (commandState) {
                        is CommandState.Processing -> SmallCircularProgressIndicator()
                        is CommandState.Idle,
                        is CommandState.Success,
                        is CommandState.Failure,
                            -> Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            onKeyboardAction = { onSave() }
        )
        AddCategoryError(commandState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTypeMenu(
    categoryType: CategoryType,
    commandState: CommandState<Command>,
    onCategoryTypeSelected: (CategoryType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .clip(MaterialTheme.shapes.extraSmall),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = MaterialTheme.space.paddingDefault),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End)
            ) {
                Icon(
                    imageVector = categoryType.icon,
                    contentDescription = categoryType.name
                )
                BasicTextField(
                    modifier = Modifier
                        .width(IntrinsicSize.Max),
                    value = categoryType.name,
                    onValueChange = { },
                    readOnly = true,
                    enabled = commandState !is CommandState.Processing
                )
                TrailingIcon(expanded = expanded)
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            CategoryType.entries.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                            Text(item.name)
                        }
                    },
                    onClick = {
                        onCategoryTypeSelected(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
private fun AddCategoryError(commandState: CommandState<Command>) {
    val message = when (commandState) {
        is CommandState.Failure -> when (commandState.err) {
            is CategoryError.DuplicatedTitle -> stringResource(Res.string.error_category_duplicated_title)
            is CategoryError.EmptyTitle -> stringResource(Res.string.error_category_empty_title)
            is CategoryError.UnknownError -> stringResource(Res.string.error_category_duplicated_title, commandState.err)
            else -> stringResource(Res.string.error_category_duplicated_title, commandState.err.message ?: "")
        }
        else -> return
    }
    Text(
        modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.space.paddingDefault),
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmallEmphasized
    )
}

val CategoryType.icon: ImageVector
    get() = when (this) {
        CategoryType.In -> Icons.Default.Download
        CategoryType.Out -> Icons.Default.Upload
    }

@Preview
@Composable
fun AddCategoryScenePreview() {
    MaterialTheme {
        AddCategoryScene(
            title = rememberTextFieldState(""),
            categoryType = CategoryType.Out,
            commandState = CommandState.Idle(),
            onSave = {},
            onCategoryTypeSelected = { _ -> },
        )
    }
}