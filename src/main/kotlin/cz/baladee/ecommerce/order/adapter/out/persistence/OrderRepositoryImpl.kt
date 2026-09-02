package cz.baladee.ecommerce.order.adapter.out.persistence

import cz.baladee.ecommerce.order.domain.model.Order
import cz.baladee.ecommerce.order.domain.repository.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val jpaRepo: OrderJpaRepository
): OrderRepository {
    override fun save(order: Order): Order {
        return jpaRepo.save(order)
    }
}