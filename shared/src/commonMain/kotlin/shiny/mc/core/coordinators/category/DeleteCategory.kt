package shiny.mc.core.coordinators.category

interface DeleteCategory {
    suspend fun deleteCategory(categoryId: Long)
}