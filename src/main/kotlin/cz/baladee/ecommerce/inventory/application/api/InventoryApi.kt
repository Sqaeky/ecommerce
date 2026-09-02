package cz.baladee.ecommerce.inventory.application.api

import cz.baladee.ecommerce.inventory.application.StockService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class InventoryApi(
    private val stockService: StockService
) {
    fun reserve(productId: UUID, quantity: Int) {
        stockService.reserveQuantity(productId, quantity)
    }

    fun release(productId: UUID, quantity: Int) {
        stockService.releaseQuantity(productId, quantity)
    }

    fun confirmReservation(productId: UUID, quantity: Int) {
        stockService.confirmReservation(productId, quantity)
    }
}