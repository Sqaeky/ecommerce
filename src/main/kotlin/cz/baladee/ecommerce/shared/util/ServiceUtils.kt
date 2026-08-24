package cz.baladee.ecommerce.shared.util

import java.text.Normalizer

fun String.toSlug(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()

    return normalized
        .replace("/", "-")
        .replace(Regex("[^a-z0-9\\s-]"), "")
        .trim()
        .replace(Regex("\\s+"), "-")
        .replace(Regex("-+"), "-")
}