package cz.baladee.ecommerce.inventory.adapter.out.persistence

import cz.baladee.ecommerce.inventory.domain.model.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StockJpaRepository : JpaRepository<Stock, UUID> {
}