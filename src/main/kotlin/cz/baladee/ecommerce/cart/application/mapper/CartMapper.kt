package cz.baladee.ecommerce.cart.application.mapper

import cz.baladee.ecommerce.cart.application.dto.CartItemResponse
import cz.baladee.ecommerce.cart.application.dto.CartResponse
import cz.baladee.ecommerce.cart.domain.model.Cart
import cz.baladee.ecommerce.cart.domain.model.CartItem
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CartMapper {

    fun toResponse(cart: Cart): CartResponse {
        return CartResponse(
            id = cart.id!!,
            userId = cart.userId,
            items = cart.items.map { toItemResponse(it) },
            totalItems = cart.items.sumOf { it.quantity },
            totalPrice = cart.items
                .map { it.priceAtAddition.multiply(BigDecimal(it.quantity)) }
                .fold(BigDecimal.ZERO) { acc, price -> acc.add(price) },
            updatedAt = cart.updatedAt
        )
    }

    fun toItemResponse(item: CartItem): CartItemResponse {
        return CartItemResponse(
            id = item.id!!,
            productId = item.productId,
            quantity = item.quantity,
            priceAtAddition = item.priceAtAddition,
            totalPrice = item.priceAtAddition.multiply(BigDecimal(item.quantity))
        )
    }
}