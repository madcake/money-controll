package shiny.mc.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ColumnItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    containerColor: Color? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onMenu: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    ColumnItem(
        modifier = modifier,
        containerColor = containerColor,
        headline = { ColumnItemTitleText(headline) },
        supporting = supporting?.let { { ColumnItemSupportText(supporting) } },
        trailing = trailing,
        leading = leading,
        onMenu = onMenu,
        onClick = onClick,
    )
}

@Composable
fun ColumnItem(
    headline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    supporting: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onMenu: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier
            .itemEventHandler(onMenu, onClick),
        headlineContent = headline,
        supportingContent = supporting?.let { { supporting() } },
        leadingContent = leading,
        trailingContent = trailing,
        colors = if (containerColor == null) ListItemDefaults.colors() else ListItemDefaults.colors().copy(containerColor = containerColor)
    )
}

@Composable
fun ColumnItemTitleText(
    text: String,
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.MiddleEllipsis
    )
}

@Composable
fun ColumnItemSupportText(
    text: String,
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.MiddleEllipsis
    )
}

@Composable
fun ColumnItemValue(
    value: String,
    supportValue: String,
) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = supportValue,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun PreviewColumnItem() {
    MaterialTheme {
        ColumnItem(
            headline = "Foo category version 1.0 for preview and with very long title or name",
            supporting = "Test testing",
            trailing = {
                ColumnItemValue(
                    value = "1000.34",
                    supportValue = "1100.89"
                )
            },
            onClick = {}
        )
    }
}