package shiny.mc.core.domain.value

sealed class PeriodError(message: String) : Exception(message) {

    class PeriodNotFound(val date: PeriodDate) : PeriodError("Period ${date.month}-${date.year} not found.")

    class PeriodExists(val date: PeriodDate) : PeriodError("Period ${date.month}-${date.year} exists.")
}