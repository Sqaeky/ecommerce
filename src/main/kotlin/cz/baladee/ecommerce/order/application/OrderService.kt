package cz.baladee.ecommerce.order.application

import cz.baladee.ecommerce.cart.application.api.CartApi
import cz.baladee.ecommerce.catalog.application.api.ProductQueryService
import cz.baladee.ecommerce.inventory.application.api.InventoryApi
import cz.baladee.ecommerce.order.application.dto.CreateOrderReq
import cz.baladee.ecommerce.order.application.dto.OrderRes
import cz.baladee.ecommerce.order.application.mapper.OrderMapper
import cz.baladee.ecommerce.order.domain.model.Order
import cz.baladee.ecommerce.order.domain.model.OrderItem
import cz.baladee.ecommerce.order.domain.model.Status
import cz.baladee.ecommerce.order.domain.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val cartApi: CartApi,
    private val inventoryApi: InventoryApi,
    private val productQueryService: ProductQueryService,
    private val mapper: OrderMapper
) {

    @Transactional
    fun createFromCart(userId: UUID, req: CreateOrderReq): OrderRes {
        val cart = cartApi.getCartForCheckout(userId)

        val order = Order(
            orderNumber = generateOrderNumber(),
            userId = userId,
            status = Status.CREATED,
            currency = "CZK",
            shippingAddressId = req.shippingAddressId,
            billingAddressId = req.billingAddressId,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        var total = BigDecimal.ZERO

        cart.items.forEach { item ->
            val productName = productQueryService.getProductForCart(item.productId).name

            val lineTotal = item.unitPrice.multiply(BigDecimal(item.quantity))
            total = total.add(lineTotal)

            order.items.add(
                OrderItem(
                    order = order,
                    productId = item.productId,
                    productName = productName,
                    quantity = item.quantity,
                    unitPrice = item.unitPrice,
                    totalPrice = lineTotal
                )
            )
        }

        order.totalPrice = total

        // 1) potvrdit rezervace ve skladu
        cart.items.forEach { item ->
            inventoryApi.confirmReservation(item.productId, item.quantity)
        }

        // 2) uložit objednávku
        val saved = orderRepository.save(order)

        // 3) vyprázdnit košík
        cartApi.clearCart(userId)

        return mapper.toResponse(saved)
    }

    private fun generateOrderNumber(): String {
        val date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) // 20260901
        val suffix = UUID.randomUUID().toString().take(8).uppercase()
        return "ORD-$date-$suffix"
    }
}