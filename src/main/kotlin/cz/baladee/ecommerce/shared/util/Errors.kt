package cz.baladee.ecommerce.shared.util

enum class Errors(val code: String) {
    UNKNOWN_EXCEPTION("000"),
    USER_ID_NOT_FOUND("001"),
    PRODUCT_ID_NOT_FOUND("002"),
    CATEGORY_ID_NOT_FOUND("003"),
    NEGATIVE_QUANTITY("004"),
    INSUFFICIENT_QUANTITY("005")
}