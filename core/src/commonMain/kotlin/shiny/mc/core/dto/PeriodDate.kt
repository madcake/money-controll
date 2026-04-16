package shiny.mc.core.dto

data class PeriodDate(
    val month: Int,
    val year: Int
) {
//    override fun toString(): String {
//        val date = LocalDate(
//            year = year,
//            month = month,
//            day = 1,
//        )
//        return periodFormatter.format(date)
//    }
//
//    fun toMillis(): Long {
//        return LocalDateTime(year = year, month = month, day = 1, 0, 0, 0, 1)
//            .toInstant(TimeZone.UTC)
//            .toEpochMilliseconds()
//    }
//
//    companion object {
//        fun default(): PeriodDate {
//            return Clock.System.now().toLocalDateTime(TimeZone.UTC).let {
//                PeriodDate(
//                    month = it.month.number,
//                    year = it.year,
//                )
//            }
//        }
//
//        fun fromMillis(millis: Long): PeriodDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).let {
//            PeriodDate(
//                month = it.month.number,
//                year = it.year,
//            )
//        }
//    }
}