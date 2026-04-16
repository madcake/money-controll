package shiny.mc.core.dto.error

sealed class TransactionError(msg: String) : Exception(msg) {
    class IncorrectRecord : TransactionError("Incorrect record")
    class IncorrectValue : TransactionError("Incorrect value")
    class IncorrectPurpose : TransactionError("Incorrect purpose")
    class InvalidDate : TransactionError("Must be include in the period")
}