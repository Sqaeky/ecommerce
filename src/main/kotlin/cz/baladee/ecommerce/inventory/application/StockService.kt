package cz.baladee.ecommerce.inventory.application

import cz.baladee.ecommerce.inventory.application.dto.Stock
import cz.baladee.ecommerce.inventory.application.mapper.StockMapper
import cz.baladee.ecommerce.inventory.domain.model.Stock as DbStock
import cz.baladee.ecommerce.inventory.domain.repository.StockRepository
import cz.baladee.ecommerce.shared.advice.exception.NegativeQuantityException
import cz.baladee.ecommerce.shared.advice.exception.InsufficientStockException
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class StockService(
    private val stockRepo: StockRepository,
    private val mapper: StockMapper,
) {

    @Transactional(readOnly = true)
    fun getStockForProduct(id: UUID): Stock {
        val stock = stockRepo.findByProductId(id)
        return mapper.toDto(stock)
    }

    @Transactional
    fun createStockForProduct(id: UUID, initialStock: Int) {
        stockRepo.save(
            DbStock(
                productId = id,
                quantity = initialStock
            )
        )
    }

    @Transactional
    fun adjustQuantity(id: UUID, quantity: Int): Stock {
        val stock = stockRepo.findByProductId(id)
        if (stock.quantity - quantity < 0) {
            throw NegativeQuantityException(Errors.NEGATIVE_QUANTITY, "Final quantity cannot go into negative number")
        }
        stock.quantity += quantity
        return mapper.toDto(stockRepo.save(stock))
    }

    @Transactional
    fun reserveQuantity(id: UUID, quantity: Int): Stock {
        if (quantity < 0) {
            throw NegativeQuantityException(Errors.NEGATIVE_QUANTITY, "Reserve quantity cannot be negative number")
        }
        val stock = stockRepo.findByProductId(id)
        if (stock.availableQuantity < quantity) {
            throw InsufficientStockException(Errors.INSUFFICIENT_QUANTITY, "Not enough stock for product $id")
        }
        stock.reservedQuantity += quantity
        return mapper.toDto(stockRepo.save(stock))
    }

    @Transactional
    fun releaseQuantity(id: UUID, quantity: Int): Stock {
        val stock = stockRepo.findByProductId(id)
        if (stock.reservedQuantity < quantity) {
            throw InsufficientStockException(Errors.INSUFFICIENT_QUANTITY, "reserve quantity is not that big")
        }
        stock.reservedQuantity -= quantity
        return mapper.toDto(stockRepo.save(stock))
    }

    @Transactional
    fun confirmReservation(id: UUID, quantity: Int) {
        val stock = stockRepo.findByProductId(id)
        if (stock.reservedQuantity < quantity) {
            throw InsufficientStockException(Errors.INSUFFICIENT_QUANTITY, "Not enough reserved stock for product $id")
        }
        stock.reservedQuantity -= quantity
        stock.quantity -= quantity
        stock.updatedAt = Instant.now()

        stockRepo.save(stock)
    }
}