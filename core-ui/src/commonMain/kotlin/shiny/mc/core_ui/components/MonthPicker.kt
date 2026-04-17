package shiny.mc.core_ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import shiny.mc.core_ui.resources.Res
import shiny.mc.core_ui.resources.apr
import shiny.mc.core_ui.resources.aug
import shiny.mc.core_ui.resources.dec
import shiny.mc.core_ui.resources.feb
import shiny.mc.core_ui.resources.jan
import shiny.mc.core_ui.resources.jul
import shiny.mc.core_ui.resources.jun
import shiny.mc.core_ui.resources.mar
import shiny.mc.core_ui.resources.may
import shiny.mc.core_ui.resources.nov
import shiny.mc.core_ui.resources.oct
import shiny.mc.core_ui.resources.sep
import shiny.mc.platform.currentDateTime

@Composable
fun MonthPicker(
    visible: Boolean,
    currentMonth: Int = currentDateTime().month.number,
    currentYear: Int = currentDateTime().year,
    onSelect: (Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    val months = listOf(
        stringResource(Res.string.jan).substring(0, 3),
        stringResource(Res.string.feb).substring(0, 3),
        stringResource(Res.string.mar).substring(0, 3),
        stringResource(Res.string.apr).substring(0, 3),
        stringResource(Res.string.may).substring(0, 3),
        stringResource(Res.string.jun).substring(0, 3),
        stringResource(Res.string.jul).substring(0, 3),
        stringResource(Res.string.aug).substring(0, 3),
        stringResource(Res.string.sep).substring(0, 3),
        stringResource(Res.string.oct).substring(0, 3),
        stringResource(Res.string.nov).substring(0, 3),
        stringResource(Res.string.dec).substring(0, 3),
    )

    var month by remember {
        mutableStateOf(months[currentMonth - 1])
    }

    var year by remember {
        mutableStateOf(currentYear)
    }

    if (visible) {
        AlertDialog(
            title = { },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(90f)
                                .clip(CircleShape)
                                .clickable { year-- },
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = year.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .rotate(-90f)
                                .clip(CircleShape)
                                .clickable { year++ },
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )

                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(0.dp),
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.Center,
                            itemVerticalAlignment = Alignment.CenterVertically,
                            maxItemsInEachRow = 3,
                        ) {
                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .clickable { month = it }
                                        .background(color = Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val animatedSize by animateDpAsState(
                                        targetValue = if (month == it) 60.dp else 0.dp,
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing,
                                        )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                color = if (month == it) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    Color.Transparent
                                                },
                                                shape = CircleShape,
                                            )
                                    )
                                    Text(
                                        text = it,
                                        color = if (month == it) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                        fontWeight = FontWeight.W600,
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onSelect(months.indexOf(month) + 1, year) },
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text(text = "Cancel")
                }
            },
            onDismissRequest = onCancel,
        )
    }
}