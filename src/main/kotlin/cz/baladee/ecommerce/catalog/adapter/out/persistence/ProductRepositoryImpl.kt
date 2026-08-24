package cz.baladee.ecommerce.catalog.adapter.out.persistence

import cz.baladee.ecommerce.catalog.domain.model.Product
import cz.baladee.ecommerce.catalog.domain.repository.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val jpaRepository: ProductJpaRepository
): ProductRepository {
    override fun save(product: Product): Product {
        return jpaRepository.save(product)
    }
}