package shiny.mc.core.domain.value

data class PeriodValues(
    val inEstimate: Double,
    val outEstimate: Double,
    val inReal: Double,
    val outReal: Double,
) {
    val diffEstimate: Double
        get() = inEstimate - outEstimate

    val estimateState: ValueState
        get() = when (diffEstimate >= 0) {
            true -> ValueState.Surplus
            false -> ValueState.Deficit
        }

    val diffReal: Double
        get() = inReal - outReal

    val realState: ValueState
        get() = when (diffReal >= 0) {
            true -> ValueState.Surplus
            false -> ValueState.Deficit
        }
}