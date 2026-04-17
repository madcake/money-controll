package shiny.mc.feature.transaction.model

sealed interface AddTransactionCommand {
    object None : AddTransactionCommand
    data class Add(val recordId: String) : AddTransactionCommand
    data class Save(
        val recordId: String,
        val purpose: String,
        val value: String,
        val date: Long,
    ) : AddTransactionCommand
}