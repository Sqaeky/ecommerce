package cz.baladee.ecommerce.order.application.dto

import cz.baladee.ecommerce.order.domain.model.Status
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderRes(
    val id: UUID,
    val orderNumber: String,
    val userId: UUID,
    val status: Status,
    val totalPrice: BigDecimal,
    val currency: String,
    val items: List<OrderItemRes>,
    val createdAt: Instant,
    val updatedAt: Instant?
)

data class OrderItemRes(
    val id: UUID,
    val productId: UUID,
    val productName: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal
)