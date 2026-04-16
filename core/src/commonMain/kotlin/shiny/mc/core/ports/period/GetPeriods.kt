package shiny.mc.core.ports.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period

interface GetPeriods {
    fun periods(): Flow<List<Period>>
}