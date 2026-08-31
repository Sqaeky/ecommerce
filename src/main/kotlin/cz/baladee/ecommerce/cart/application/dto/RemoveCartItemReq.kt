package cz.baladee.ecommerce.cart.application.dto

import java.util.UUID

data class RemoveCartItemReq(
    val productId: UUID
)