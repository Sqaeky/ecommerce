package cz.baladee.ecommerce.catalog.adapter.out.persistence

import cz.baladee.ecommerce.catalog.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductJpaRepository: JpaRepository<Product, UUID> {
}