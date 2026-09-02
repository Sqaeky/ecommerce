package cz.baladee.ecommerce.order.application.mapper

import cz.baladee.ecommerce.order.application.dto.OrderItemRes
import cz.baladee.ecommerce.order.application.dto.OrderRes
import cz.baladee.ecommerce.order.domain.model.Order
import cz.baladee.ecommerce.order.domain.model.OrderItem
import org.springframework.stereotype.Component

@Component
class OrderMapper {

    fun toResponse(order: Order): OrderRes {
        return OrderRes(
            id = order.id!!,
            orderNumber = order.orderNumber,
            userId = order.userId,
            status = order.status,
            totalPrice = order.totalPrice,
            currency = order.currency,
            items = order.items.map { toItemResponse(it) },
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )
    }

    fun toItemResponse(item: OrderItem): OrderItemRes {
        return OrderItemRes(
            id = item.id!!,
            productId = item.productId,
            productName = item.productName,
            quantity = item.quantity,
            unitPrice = item.unitPrice,
            totalPrice = item.totalPrice
        )
    }
}