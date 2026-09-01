package cz.baladee.ecommerce.cart.application

import cz.baladee.ecommerce.cart.application.dto.AddToCartRequest
import cz.baladee.ecommerce.cart.application.dto.CartResponse
import cz.baladee.ecommerce.cart.application.dto.RemoveCartItemReq
import cz.baladee.ecommerce.cart.application.dto.UpdateCartReq
import cz.baladee.ecommerce.cart.application.mapper.CartMapper
import cz.baladee.ecommerce.cart.domain.model.CartItem
import cz.baladee.ecommerce.cart.domain.repository.CartRepository
import cz.baladee.ecommerce.catalog.application.api.ProductQueryService
import cz.baladee.ecommerce.inventory.application.api.InventoryApi
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import cz.baladee.ecommerce.cart.domain.model.Cart as DbCart

@Service
class CartService(
    private val cartRepo: CartRepository,
    private val mapper: CartMapper,
    private val inventory: InventoryApi,
    private val productQueryService: ProductQueryService
) {

    @Transactional
    fun getCart(userId: UUID): CartResponse {
        val cart = getOrCreateCart(userId)
        return mapper.toResponse(cart)
    }

    private fun getOrCreateCart(userId: UUID): DbCart {
        return cartRepo.findByUserId(userId)
            ?: cartRepo.save(
                DbCart(
                    userId = userId,
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
    }

    @Transactional
    fun addCartItem(userId: UUID, req: AddToCartRequest): CartResponse {
        require(req.quantity > 0) { "Quantity must be above 0" }

        val cart = getOrCreateCart(userId)

        // Find Product in Catalog
        val product = productQueryService.getProductForCart(req.productId)

        // Stock reservation
        inventory.reserve(req.productId, req.quantity)

        // item check
        val existingItem = cart.items.find { it.productId == req.productId }

        if (existingItem != null) {
            existingItem.quantity += req.quantity
            existingItem.updatedAt = Instant.now()
        } else {
            val newItem = CartItem(
                cart = cart,
                productId = req.productId,
                quantity = req.quantity,
                priceAtAddition = product.price,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
            cart.items.add(newItem)
        }

        cart.updatedAt = Instant.now()
        val saved = cartRepo.save(cart)

        return mapper.toResponse(saved)
    }

    @Transactional
    fun updateCartItem(userId: UUID, req: UpdateCartReq): CartResponse {
        require(req.quantity > 0) { "Quantity must be above 0"  }
        val cart = getOrCreateCart(userId)

        val item = cart.items.find { it.productId == req.productId }
        if (item == null) {
            throw NotFoundException(Errors.CART_ITEM_MISSING)
        }

        val diff = req.quantity - item.quantity

        when {
            diff > 0 -> inventory.reserve(req.productId, diff)
            diff < 0 -> inventory.release(req.productId, -diff)
        }

        item.quantity = req.quantity
        item.updatedAt = Instant.now()

        cart.updatedAt = Instant.now()
        return mapper.toResponse(cartRepo.save(cart))
    }

    @Transactional
    fun removeCartItem(userId: UUID, req: RemoveCartItemReq): CartResponse {
        val cart = getOrCreateCart(userId)

        val existingItem = cart.items.find { it.productId == req.productId }
            ?: throw NotFoundException(Errors.CART_ITEM_MISSING)

        inventory.release(existingItem.productId, existingItem.quantity)

        cart.items.remove(existingItem)

        return mapper.toResponse(cartRepo.save(cart))
    }
}