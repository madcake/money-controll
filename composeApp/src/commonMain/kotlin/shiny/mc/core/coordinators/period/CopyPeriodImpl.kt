package shiny.mc.core.coordinators.period

import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.repositories.CategoryRepository

class CopyPeriodImpl(
    private val categoryRepository: CategoryRepository
) : CopyPeriod {
    override suspend fun copy(
        from: PeriodDate,
        to: PeriodDate
    ) {
        categoryRepository.copyPeriod(from, to)
    }
}