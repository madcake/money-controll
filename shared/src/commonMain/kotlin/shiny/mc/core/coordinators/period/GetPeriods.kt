package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period

interface GetPeriods {
    fun periods(): Flow<List<Period>>
}