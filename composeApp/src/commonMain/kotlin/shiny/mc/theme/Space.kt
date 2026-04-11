package shiny.mc.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Space {
    val paddingDefault: Dp = 16.dp
    val dividerArrangement = Arrangement.spacedBy(1.dp)
}

val MaterialTheme.space: Space
    get() = Space