package cz.baladee.ecommerce.catalog.adapter.out.persistence

import cz.baladee.ecommerce.catalog.domain.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CategoryJpaRepository: JpaRepository<Category, UUID> {
}