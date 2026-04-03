package shiny.mc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform