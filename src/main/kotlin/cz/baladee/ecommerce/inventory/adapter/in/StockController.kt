package cz.baladee.ecommerce.inventory.adapter.`in`

import cz.baladee.ecommerce.inventory.application.dto.Stock
import cz.baladee.ecommerce.inventory.application.stock.StockService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/inventory")
class StockController(
    private val service: StockService
) {

    @GetMapping("/stock/{id}")
    fun getStockForProduct(@PathVariable id: UUID): Stock {
        return service.getStockForProduct(id)
    }

    @PostMapping("/stock/{id}/create")
    fun createStockForProduct(@PathVariable id: UUID) {
        service.createStockForProduct(id)
    }

    @PostMapping("/stock/{id}")
    fun adjustQuantity(
        @PathVariable id: UUID,
        @RequestParam(value = "quantity", defaultValue = "0") quantity: Int
    ): Stock {
        return service.adjustQuantity(id, quantity)
    }
}