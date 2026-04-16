package shiny.mc.core.dto.error

sealed class CategoryError(msg: String) : Exception(msg) {
    class CategoryNotFound : CategoryError("Category not found")
    class UnknownError(msg: String) : CategoryError(msg)
    class EmptyTitle: CategoryError("Category title couldn't emppty")
    class DuplicatedTitle: CategoryError("Invalid category title")
}