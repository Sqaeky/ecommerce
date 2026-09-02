package cz.baladee.ecommerce.order.adapter.`in`

import cz.baladee.ecommerce.order.application.OrderService
import cz.baladee.ecommerce.order.application.dto.CreateOrderReq
import cz.baladee.ecommerce.order.application.dto.OrderRes
import cz.baladee.ecommerce.user.application.api.UserAuthentication
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(
        authentication: Authentication,
        @RequestBody @Valid req: CreateOrderReq
    ): ResponseEntity<OrderRes> {
        val userId = UserAuthentication.userId(authentication)
        val order = orderService.createFromCart(userId, req)
        return ResponseEntity.status(HttpStatus.CREATED).body(order)
    }
}