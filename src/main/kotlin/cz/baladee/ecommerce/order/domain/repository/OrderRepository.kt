package cz.baladee.ecommerce.order.domain.repository

import cz.baladee.ecommerce.order.domain.model.Order

interface OrderRepository {
    fun save(order: Order): Order
}