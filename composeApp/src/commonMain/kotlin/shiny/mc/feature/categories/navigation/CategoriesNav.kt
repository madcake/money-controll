package shiny.mc.feature.categories.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.core.dto.PeriodDate
import shiny.mc.feature.categories.CategoriesNavScreen
import shiny.mc.navigation.OnCancel

@Serializable
class CategoriesNavKey(
    val month: Int,
    val year: Int,
) : NavKey


fun MutableList<NavKey>.openCategories(periodDate: PeriodDate) {
    add(CategoriesNavKey(periodDate.month,periodDate.year))
}

fun EntryProviderScope<NavKey>.categories(
    onCancel: OnCancel,
) {
    entry<CategoriesNavKey> { entry ->
        CategoriesNavScreen(PeriodDate(entry.month, entry.year), onCancel = onCancel)
    }
}