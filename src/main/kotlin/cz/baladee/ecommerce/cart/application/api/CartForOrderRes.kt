package cz.baladee.ecommerce.cart.application.api

import java.math.BigDecimal
import java.util.UUID

data class CartForOrderRes(
    val cartId: UUID,
    val items: List<CartItemForOrderRes>,
)

data class CartItemForOrderRes(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal,
)
