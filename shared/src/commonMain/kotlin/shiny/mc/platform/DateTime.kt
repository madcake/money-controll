package shiny.mc.platform

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

val dateFormatter = LocalDate.Format {
    day();
    char(' ')
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year()
}

val periodFormatter = LocalDate.Format {
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year()
}

fun currentDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()) = Instant
    .fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
    .toLocalDateTime(timeZone = timeZone)

fun Long.dateFormate(): String {
    val dateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    return dateFormatter.format(dateTime.date)
}

