package cz.baladee.ecommerce.inventory.application.stock

import cz.baladee.ecommerce.inventory.application.dto.Stock
import cz.baladee.ecommerce.inventory.application.mapper.StockMapper
import cz.baladee.ecommerce.inventory.domain.model.Stock as DbStock
import cz.baladee.ecommerce.inventory.domain.repository.StockRepository
import cz.baladee.ecommerce.shared.advice.exception.IllegalNegativeNumber
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StockService(
    private val stockRepo: StockRepository,
    private val mapper: StockMapper,
) {

    fun getStockForProduct(id: UUID): Stock {
        val stock = stockRepo.findByProductId(id)
        return mapper.toDto(stock)
    }

    fun createStockForProduct(id: UUID) {
        stockRepo.save(DbStock(
            productId = id
        ))
    }

    fun adjustQuantity(id: UUID, quantity: Long): Stock {
        if (quantity < 0) {
            throw IllegalNegativeNumber(Errors.NEGATIVE_QUANTITY, "Quantity cannot be negative number")
        }
        val stock = stockRepo.findByProductId(id)
        stock.quantity = quantity
        return mapper.toDto(stockRepo.save(stock))
    }
}