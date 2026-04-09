package shiny.mc.services.store.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.domain.value.PeriodValues

data class PeriodViewEntity(
    val month: Int,
    val year: Int,
    val assetScheduled: Double,
    val assetReal: Double,
    val liabilityScheduled: Double,
    val liabilityReal: Double,
) {
    fun diffScheduled() = assetScheduled - liabilityScheduled

    fun diffReal() = assetReal - liabilityReal
}

fun PeriodViewEntity.toDto() = Period(
    date = PeriodDate(month, year),
    values = PeriodValues(
        assetScheduled = assetScheduled,
        assetReal = assetReal,
        liabilityScheduled = liabilityScheduled,
        liabilityReal = liabilityReal,
    )
)

fun List<PeriodViewEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<PeriodViewEntity>>.toDto() = mapLatest { it.toDto() }