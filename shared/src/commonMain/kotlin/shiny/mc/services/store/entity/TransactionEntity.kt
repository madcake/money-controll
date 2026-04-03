package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryRecordId"],
            onDelete = CASCADE,
        ),
        ForeignKey(
            entity = TokenEntity::class,
            parentColumns = ["id"],
            childColumns = ["tokenId"],
            onDelete = NO_ACTION,
        ),
    ]
)
class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val categoryRecordId: String,
    val value: Float,
    val datetime: Long? = null,
    val tokenId: Int? = null,
)