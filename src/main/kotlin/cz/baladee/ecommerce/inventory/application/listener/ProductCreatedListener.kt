package cz.baladee.ecommerce.inventory.application.listener

import cz.baladee.ecommerce.shared.event.ProductCreatedEvent
import cz.baladee.ecommerce.inventory.application.StockService
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
class ProductCreatedListener(
    private val service: StockService
) {
    @ApplicationModuleListener
    fun onProductCreated(event: ProductCreatedEvent) {
        service.createStockForProduct(
            event.id,
            event.initialStock ?: 0
        )
    }
}