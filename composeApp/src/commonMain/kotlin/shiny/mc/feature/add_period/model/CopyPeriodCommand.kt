package shiny.mc.feature.add_period.model

import shiny.mc.core.dto.PeriodDate

sealed interface AddPeriodCommand {
    object None : AddPeriodCommand

    object Reset : AddPeriodCommand

    class Add(val date: PeriodDate) : AddPeriodCommand

    class CopySelectTo(val from: PeriodDate) : AddPeriodCommand

    class Copy(val from: PeriodDate, val to: PeriodDate) : AddPeriodCommand
}