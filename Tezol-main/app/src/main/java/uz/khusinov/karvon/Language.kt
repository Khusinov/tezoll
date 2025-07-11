package uz.khusinov.karvon

enum class Language(val langCode: String) {
    UZ("uz"),
    RU("ru")
}

fun String.toLanguage(): Language {
    return when (this) {
        Language.UZ.langCode -> Language.UZ
        Language.RU.langCode -> Language.RU
        else -> throw IllegalArgumentException("Invalid language code: $this")
    }
}