package cz.baladee.ecommerce.inventory.application.mapper

import cz.baladee.ecommerce.inventory.application.dto.Stock
import cz.baladee.ecommerce.inventory.domain.model.Stock as DbStock
import org.springframework.stereotype.Component

@Component
class StockMapper {

    fun toDto(dbStock: DbStock): Stock {
        return Stock(
            dbStock.productId,
            dbStock.availableQuantity,
            dbStock.reservedQuantity
        )
    }
}