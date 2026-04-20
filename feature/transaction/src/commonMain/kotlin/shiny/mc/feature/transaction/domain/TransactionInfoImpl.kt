package shiny.mc.feature.transaction.domain

import androidx.compose.runtime.Stable
import shiny.mc.core.domain.entity.TransactionInfo
import shiny.mc.core.dto.Transaction
import shiny.mc.platform.dateFormate
import shiny.mc.platform.format

@Stable
class TransactionInfoImpl(dto: Transaction) : TransactionInfo {
    override val id: Long = dto.id ?: 0
    override val title: String = dto.purpose
    override val value: String = dto.value.format()
    override val detailedValue: String = ""
    override val date: String = dto.datetime.dateFormate()
}