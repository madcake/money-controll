package shiny.mc.feature.add_category

sealed interface AddCategoryCmd {
    object None : AddCategoryCmd
    class Save : AddCategoryCmd
}