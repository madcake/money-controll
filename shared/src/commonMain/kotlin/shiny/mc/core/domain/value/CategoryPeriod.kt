package shiny.mc.core.domain.value

sealed interface CategoryPeriod {
    object Day : CategoryPeriod
    object Week : CategoryPeriod
    object Month : CategoryPeriod
    object Season : CategoryPeriod
    object Year : CategoryPeriod
}