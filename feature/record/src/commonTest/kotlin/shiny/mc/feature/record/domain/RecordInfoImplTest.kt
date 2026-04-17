package shiny.mc.feature.record.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.Record
import shiny.mc.core.dto.ValueState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecordInfoImplTest {

    @Test
    fun constructor_mapsFieldsCorrectly() {
        val category = Category(
            id = 123L,
            title = "Groceries",
            type = CategoryType.Out
        )
        val dto = Record(
            id = "rec_001",
            category = category,
            month = 5,
            year = 2024,
            estimateValue = 500.0,
            realValue = 450.0
        )

        val recordInfo = RecordInfoImpl(dto, "USD")

        assertEquals("rec_001", recordInfo.id)
        assertEquals(123L, recordInfo.categoryId)
        assertEquals(CategoryType.Out, recordInfo.categoryType)
        assertEquals("Groceries", recordInfo.title)
        assertEquals("500,00 \$", recordInfo.estimateValue)
        assertEquals("450,00 \$", recordInfo.realValue)
    }

    @Test
    fun valueState_isSurplus_whenEstimateIsGreaterThanReal() {
        val category = Category(id = 1, title = "Test", type = CategoryType.Out)
        val dto = Record(
            id = "1",
            category = category,
            month = 1,
            year = 2024,
            estimateValue = 100.0,
            realValue = 80.0
        )

        val recordInfo = RecordInfoImpl(dto, "USD")

        assertEquals(ValueState.Surplus, recordInfo.valueState)
    }

    @Test
    fun valueState_isSurplus_whenEstimateIsEqualToReal() {
        val category = Category(id = 1, title = "Test", type = CategoryType.Out)
        val dto = Record(
            id = "1",
            category = category,
            month = 1,
            year = 2024,
            estimateValue = 100.0,
            realValue = 100.0
        )

        val recordInfo = RecordInfoImpl(dto, "USD")

        assertEquals(ValueState.Surplus, recordInfo.valueState)
    }

    @Test
    fun valueState_isDeficit_whenEstimateIsLessThanReal() {
        val category = Category(id = 1, title = "Test", type = CategoryType.Out)
        val dto = Record(
            id = "1",
            category = category,
            month = 1,
            year = 2024,
            estimateValue = 80.0,
            realValue = 100.0
        )

        val recordInfo = RecordInfoImpl(dto, "USD")

        assertEquals(ValueState.Deficit, recordInfo.valueState)
    }
}
