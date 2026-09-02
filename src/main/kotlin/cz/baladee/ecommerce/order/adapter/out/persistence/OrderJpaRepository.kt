package cz.baladee.ecommerce.order.adapter.out.persistence

import cz.baladee.ecommerce.order.domain.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderJpaRepository: JpaRepository<Order, UUID> {
}