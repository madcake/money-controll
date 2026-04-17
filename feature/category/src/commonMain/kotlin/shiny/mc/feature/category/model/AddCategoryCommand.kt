package shiny.mc.feature.category.model

sealed interface AddCategoryCommand {
    object None : AddCategoryCommand
    class Save : AddCategoryCommand
}