package cz.baladee.ecommerce.catalog.adapter.out.persistence

import cz.baladee.ecommerce.catalog.domain.model.Category
import cz.baladee.ecommerce.catalog.domain.repository.CategoryRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CategoryRepositoryImpl(
    private val jpaRepo: CategoryJpaRepository
): CategoryRepository {
    override fun findById(id: UUID): Category? {
        return jpaRepo.findById(id).orElse(null)
    }

    override fun save(category: Category): Category {
        return jpaRepo.save(category)
    }
}