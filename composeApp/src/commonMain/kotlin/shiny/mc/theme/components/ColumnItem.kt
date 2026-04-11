package shiny.mc.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import shiny.mc.theme.MCTheme

@Composable
fun ColumnItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    containerColor: Color? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    position: ItemPosition = ItemPosition.Single,
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
        position = position,
        onMenu = onMenu,
        onClick = onClick,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ColumnItem(
    headline: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    supporting: @Composable (() -> Unit)? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    position: ItemPosition,
    onMenu: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    SegmentedListItem(
        modifier = modifier,
        onClick = onClick ?: {},
        shapes = ListItemDefaults.segmentedShapes(position.index, position.count),
        supportingContent = supporting?.let { { supporting() } },
        leadingContent = leading,
        trailingContent = trailing,
        content = headline,
        colors = ListItemDefaults.segmentedColors(
            containerColor = containerColor ?: Color.Unspecified
        )
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
    valueColor: Color = Color.Unspecified,
    supportColor: Color = Color.Unspecified,
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = value,
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = supportValue,
            color = supportColor,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


@Preview
@Composable
fun PreviewColumnItem() {
    MCTheme {
        ColumnItem(
            headline = "Foo category version 1.0 for preview and with very long title or name",
            supporting = "Test testing",
            trailing = {
                ColumnItemValue(
                    value = "1000.34",
                    supportValue = "0"
                )
            },
            onClick = {}
        )
    }
}