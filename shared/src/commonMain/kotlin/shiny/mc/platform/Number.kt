package shiny.mc.platform

fun String.parseToDouble(): Double {
    val parts = trim().replace(",", ".")
        .replace(" ", "")
        .split(".")
    val number = List(parts.size) { i ->
        "${parts[i]}${if (i + 1 == parts.size - 1) "." else ""}"
    }.joinToString("")
    return number.trim().replace("\uFEFF", "").toDouble()
}

fun String.parseToDoubleOrNull(): Double? {
    return try {
        parseToDouble()
    } catch (_: Throwable) {
        null
    }
}