package cz.baladee.ecommerce.inventory.domain.repository

import cz.baladee.ecommerce.inventory.domain.model.Stock
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StockRepository {

    fun findByProductId(id: UUID): Stock

    fun save(stock: Stock): Stock
}