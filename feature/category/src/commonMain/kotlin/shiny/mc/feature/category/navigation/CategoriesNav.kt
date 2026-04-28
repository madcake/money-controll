package shiny.mc.feature.category.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core_ui.model.OnCancel
import shiny.mc.feature.category.presentation.categories.CategoriesNavScreen

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
    metadata: Map<String, Any> = emptyMap()
) {
    entry<CategoriesNavKey>(
        metadata = metadata,
    ) { entry ->
        CategoriesNavScreen(
            date = PeriodDate(entry.month, entry.year),
            onCancel = onCancel
        )
    }
}