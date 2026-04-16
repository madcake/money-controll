package shiny.mc.core.ports.category

interface DeleteCategory {
    suspend fun deleteCategory(categoryId: Long)
}