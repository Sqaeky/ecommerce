package cz.baladee.ecommerce.cart.domain.repository

import cz.baladee.ecommerce.cart.domain.model.Cart
import java.util.UUID

interface CartRepository {
    fun findByUserId(id: UUID): Cart?

    fun save(cart: Cart): Cart
}