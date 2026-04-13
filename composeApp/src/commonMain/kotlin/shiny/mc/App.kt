package shiny.mc

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shiny.mc.navigation.NavGraph
import shiny.mc.theme.MCTheme

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun App() {
    MCTheme {
        NavGraph()
    }
}