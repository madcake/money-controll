package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val description: String,
    val image: String,
    val repeat: RepeatType,
    val plannedValue: Double,
    val realValue: Double,
    val type: ExpenseType,
    val parent: Int? = null,
    val order: Int,
    val createAt: Long,
    val updatedAt: Long,
)

enum class RepeatType {
    MONTH,
}

enum class ExpenseType {
    Order,
    Schedule,
}
