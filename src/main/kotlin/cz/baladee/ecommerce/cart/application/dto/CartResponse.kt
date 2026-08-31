package cz.baladee.ecommerce.cart.application.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class CartResponse(
    val id: UUID,
    val userId: UUID?,
    val items: List<CartItemResponse>,
    val totalItems: Int,
    val totalPrice: BigDecimal,
    val updatedAt: Instant?
)