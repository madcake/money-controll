package shiny.mc.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Space {
    val paddingHalfSmall: Dp = 4.dp
    val paddingDefault: Dp = 16.dp
    val dividerArrangement = Arrangement.spacedBy(1.dp)
    val groupSpace = @Composable { Spacer(Modifier.size(paddingDefault)) }
}

val MaterialTheme.space: Space
    get() = Space

fun Modifier.paddingDefault() = Modifier.padding(Space.paddingDefault)

fun Modifier.paddingHalfSmall() = Modifier.padding(Space.paddingHalfSmall)