package cz.baladee.ecommerce.inventory.application.dto

import java.util.UUID

data class Stock(
    val productId: UUID,
    val quantityInStock: Long,
    val reserveInStock: Long,
)
