package shiny.mc.core.domain.value

sealed class RecordError(msg: String = "") : Exception(msg) {
    class CategoryNoneExist() : RecordError("Category none exists")
}