package cz.baladee.ecommerce.inventory.domain.repository

import cz.baladee.ecommerce.inventory.domain.model.Stock
import java.util.UUID

interface StockRepository {

    fun findByProductId(id: UUID): Stock

    fun save(stock: Stock): Stock
}