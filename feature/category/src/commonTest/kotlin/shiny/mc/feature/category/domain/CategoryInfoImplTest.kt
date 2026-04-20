package shiny.mc.feature.category.domain

import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryInfoImplTest {

    @Test
    fun primaryConstructor_setsFieldsCorrectly() {
        val id = 1L
        val title = "Test Category"
        val type = CategoryType.In
        val icon = "some icon"

        val categoryInfo = CategoryInfoImpl(id, title, type, icon)

        assertEquals(id, categoryInfo.id)
        assertEquals(title, categoryInfo.title)
        assertEquals(type, categoryInfo.type)
        assertEquals(icon, categoryInfo.icon)
    }

    @Test
    fun secondaryConstructor_mapsCategoryFieldsCorrectly() {
        val category = Category(
            id = 10L,
            title = "Groceries",
            type = CategoryType.Out
        )

        val categoryInfo = CategoryInfoImpl(category)

        assertEquals(10L, categoryInfo.id)
        assertEquals("Groceries", categoryInfo.title)
        assertEquals(CategoryType.Out, categoryInfo.type)
    }
}
