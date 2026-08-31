package cz.baladee.ecommerce.cart.application.dto

import java.math.BigDecimal
import java.util.UUID

data class CartItemResponse(
    val id: UUID,
    val productId: UUID,
    val quantity: Int,
    val priceAtAddition: BigDecimal,
    val totalPrice: BigDecimal
)