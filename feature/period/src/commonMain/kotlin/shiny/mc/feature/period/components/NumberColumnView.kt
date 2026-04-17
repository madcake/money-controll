package shiny.mc.feature.period.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import shiny.mc.core.dto.PeriodValues
import shiny.mc.core.dto.ValueState
import shiny.mc.core.dto.ValueType
import shiny.mc.core_ui.resources.Res
import shiny.mc.core_ui.resources.common_estimate
import shiny.mc.core_ui.resources.common_real
import shiny.mc.core_ui.theme.MCTheme
import shiny.mc.platform.format

@Composable
fun NumberColumnView(
    values: PeriodValues,
    type: ValueType,
    modifier: Modifier = Modifier,
) {
    when(type) {
        ValueType.Estimate -> NumberColumnView(
            modifier = modifier,
            title = stringResource(Res.string.common_estimate),
            inValue = values.inEstimate.format(),
            outValue = values.outEstimate.format(),
            diff = values.diffEstimate.format(),
            state = values.estimateState,
            horizontalAlignment = Alignment.End,
        )
        ValueType.Real -> NumberColumnView(
            modifier = modifier,
            title = stringResource(Res.string.common_real),
            inValue = values.inReal.format(),
            outValue = values.outReal.format(),
            diff = values.diffReal.format(),
            state = values.realState,
            horizontalAlignment = Alignment.Start,
        )
    }
}

@Composable
fun NumberColumnView(
    title: String,
    inValue: String,
    outValue: String,
    diff: String,
    state: ValueState,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(text = inValue)
        Text(text = "-$outValue")
        HorizontalDivider()
        Text(
            text = diff,
            style = MaterialTheme.typography.titleMedium,
            color = when (state) {
                ValueState.Deficit -> MaterialTheme.colorScheme.error
                ValueState.Surplus -> MaterialTheme.colorScheme.tertiary
            }
        )
    }
}

@Preview
@Composable
fun NumberColumnViewPreview() {
    MCTheme {
        NumberColumnView(
            "Estimate",
            "5 000.00",
            "7 140.00",
            "2 140.00",
            ValueState.Deficit,
            horizontalAlignment = Alignment.End
        )
    }
}