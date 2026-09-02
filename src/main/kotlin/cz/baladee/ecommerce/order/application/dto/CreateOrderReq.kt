package cz.baladee.ecommerce.order.application.dto

import java.util.UUID

data class CreateOrderReq(
    val shippingAddressId: UUID,
    val billingAddressId: UUID
)