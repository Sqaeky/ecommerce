package cz.baladee.ecommerce.cart.adapter.out.persistence

import cz.baladee.ecommerce.cart.domain.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CartJpaRepository: JpaRepository<Cart, UUID> {
    fun findByUserId(userId: UUID): Cart?
}