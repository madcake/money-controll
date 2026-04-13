package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.entity.Transaction

@Entity(
    tableName = "record_transaction",
    indices = [
        Index("recordId", name = "transaction_record_id_idx")
    ],
    foreignKeys = [
        ForeignKey(
            entity = RecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["recordId"],
            onDelete = CASCADE,
        ),
    ]
)
class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val recordId: String,
    val value: Double,
    val purpose: String,
    val datetime: Long,
)

fun TransactionEntity.toDto() = Transaction(
    id = id,
    purpose = purpose,
    value = value,
    datetime = datetime,
)

fun List<TransactionEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<TransactionEntity>>.toDto() = mapLatest { it.toDto() }

fun Transaction.toEntity(recordId: String) = TransactionEntity(
    id = id ?: 0,
    recordId = recordId,
    purpose = purpose,
    value = value,
    datetime = datetime,
)

fun List<Transaction>.toEntity(recordId: String) = map { it.toEntity(recordId) }