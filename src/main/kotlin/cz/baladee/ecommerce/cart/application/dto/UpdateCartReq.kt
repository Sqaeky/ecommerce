package cz.baladee.ecommerce.cart.application.dto

import java.util.UUID

data class UpdateCartReq(
    val productId: UUID,
    val quantity: Int = 1
)
