package cz.baladee.ecommerce.cart.adapter.`in`

import cz.baladee.ecommerce.cart.application.CartService
import cz.baladee.ecommerce.cart.application.dto.AddToCartRequest
import cz.baladee.ecommerce.cart.application.dto.CartResponse
import cz.baladee.ecommerce.user.application.api.UserAuthentication
import jakarta.validation.Valid
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController(
    private val service: CartService
) {

    @GetMapping
    fun getCart(authentication: Authentication): CartResponse {
        val userId = UserAuthentication.userId(authentication)
        return service.getCart(userId)
    }

    @PostMapping("/items")
    fun addCartItem(
        authentication: Authentication,
        @RequestBody @Valid req: AddToCartRequest
    ): CartResponse {
        val userId = UserAuthentication.userId(authentication)
        return service.addCartItem(userId, req)
    }
}