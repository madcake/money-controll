package shiny.mc.feature.categories.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.navigation.OnCancel
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.feature.categories.CategoriesNavScreen

@Serializable
class CategoriesNavKey(
    val date: PeriodDate,
) : NavKey


fun MutableList<NavKey>.openCategories(periodDate: PeriodDate) {
    add(CategoriesNavKey(periodDate))
}

fun EntryProviderScope<NavKey>.categories(
    onCancel: OnCancel,
) {
    entry<CategoriesNavKey> { entry ->
        CategoriesNavScreen(entry.date, onCancel = onCancel)
    }
}