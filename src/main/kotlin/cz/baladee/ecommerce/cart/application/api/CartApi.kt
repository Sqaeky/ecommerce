package cz.baladee.ecommerce.cart.application.api

import cz.baladee.ecommerce.cart.application.CartService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartApi(
    private val cartService: CartService
) {

    fun getCartForCheckout(userId: UUID): CartForOrderRes {
        return cartService.getCartForCheckout(userId)

    }

    fun clearCart(userId: UUID) {
        cartService.clearCart(userId)
    }
}