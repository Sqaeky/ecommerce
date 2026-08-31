package cz.baladee.ecommerce.cart.adapter.out.persistence

import cz.baladee.ecommerce.cart.domain.model.Cart
import cz.baladee.ecommerce.cart.domain.repository.CartRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CartRepositoryImpl(
    private val jpaRepo: CartJpaRepository
): CartRepository {
    override fun findByUserId(id: UUID): Cart? {
        return jpaRepo.findByUserId(id)
    }

    override fun save(cart: Cart): Cart {
        return jpaRepo.save(cart)
    }
}