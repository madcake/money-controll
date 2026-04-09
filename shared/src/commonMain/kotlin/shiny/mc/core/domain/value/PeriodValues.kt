package shiny.mc.core.domain.value

data class PeriodValues(
    val assetScheduled: Double,
    val assetReal: Double,
    val liabilityScheduled: Double,
    val liabilityReal: Double,
)