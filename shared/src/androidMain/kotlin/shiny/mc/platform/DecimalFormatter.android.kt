package shiny.mc.platform

actual fun Double.format(currencyCode: String): String {
    val format = java.text.NumberFormat.getCurrencyInstance()
    format.currency = java.util.Currency.getInstance(currencyCode)
    return format.format(this)
}