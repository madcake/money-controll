package shiny.mc.feature.transaction.model

sealed interface AddTransactionCommand {
    data class ChangeRecord(val recordId: String) : AddTransactionCommand

    data class ChangePurpose(val value: String) : AddTransactionCommand

    data class ChangeDate(val value: Long): AddTransactionCommand

    data class ChangeValue(val value: String): AddTransactionCommand

    data class Save(
        val recordId: String,
        val purpose: String,
        val value: String,
        val date: Long,
    ) : AddTransactionCommand
}