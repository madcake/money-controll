package shiny.mc.core.domain.value

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import shiny.mc.core.dto.PeriodDate
import kotlin.time.Clock
import kotlin.time.Instant

val periodFormatter = LocalDate.Format {
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year()
}

fun PeriodDate.format(): String {
    val date = LocalDate(
        year = year,
        month = month,
        day = 1,
    )
    return periodFormatter.format(date)
}

fun PeriodDate.toMillis(): Long {
    return LocalDateTime(year = year, month = month, day = 1, 0, 0, 0, 1)
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds()
}

fun PeriodDate.Companion.default(): PeriodDate {
    return Clock.System.now().toLocalDateTime(TimeZone.UTC).let {
        PeriodDate(
            month = it.month.number,
            year = it.year,
        )
    }
}

fun PeriodDate.Companion.fromMillis(millis: Long): PeriodDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).let {
    PeriodDate(
        month = it.month.number,
        year = it.year,
    )
}