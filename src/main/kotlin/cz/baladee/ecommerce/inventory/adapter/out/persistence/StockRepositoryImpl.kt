package cz.baladee.ecommerce.inventory.adapter.out.persistence

import cz.baladee.ecommerce.inventory.domain.model.Stock
import cz.baladee.ecommerce.inventory.domain.repository.StockRepository
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import java.util.UUID

class StockRepositoryImpl(
    private val jpaRepo: StockJpaRepository
): StockRepository {
    override fun findByProductId(id: UUID): Stock {
        return jpaRepo.findById(id).orElseThrow { NotFoundException(Errors.PRODUCT_ID_NOT_FOUND) }
    }

    override fun save(stock: Stock): Stock {
        return jpaRepo.save(stock)
    }
}