package shiny.mc.core.dto

/**
 * Represents a full financial period, combining a date and its associated summary values.
 *
 * @property date The month and year of the period.
 * @property values The summary financial values (estimates and actuals) for this period.
 */
data class Period(
    val date: PeriodDate,
    val values: PeriodValues,
)
