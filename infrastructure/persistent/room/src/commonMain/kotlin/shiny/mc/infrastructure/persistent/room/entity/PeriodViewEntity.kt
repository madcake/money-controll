package shiny.mc.infrastructure.persistent.room.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.PeriodValues

data class PeriodViewEntity(
    val month: Int,
    val year: Int,
    val inEstimate: Double,
    val inReal: Double,
    val outEstimate: Double,
    val outReal: Double,
)

fun PeriodViewEntity.toDto() = Period(
    date = PeriodDate(month, year),
    values = PeriodValues(
        inEstimate = inEstimate,
        inReal = inReal,
        outEstimate = outEstimate,
        outReal = outReal,
    )
)

fun List<PeriodViewEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<PeriodViewEntity>>.toDto() = mapLatest { it.toDto() }