package shiny.mc.core.domain.value

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

data class PeriodDate(
    val month: Int,
    val year: Int
) {
    fun toMillis(): Long {
        return LocalDateTime(year = year, month = month, day = 1, 0, 0, 0, 1)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

    companion object {
        fun default(): PeriodDate {
            return Clock.System.now().toLocalDateTime(TimeZone.UTC).let {
                PeriodDate(
                    month = it.month.number,
                    year = it.year,
                )
            }
        }

        fun fromMillis(millis: Long): PeriodDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).let {
            PeriodDate(
                month = it.month.number,
                year = it.year,
            )
        }
    }
}