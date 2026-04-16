package shiny.mc.core.dto.error

sealed class RecordError(msg: String = "") : Exception(msg) {
    class CategoryNoneExist() : RecordError("Category none exists")
}