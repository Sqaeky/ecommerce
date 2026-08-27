package cz.baladee.ecommerce.catalog.adapter.out.persistence

import cz.baladee.ecommerce.catalog.domain.model.Product
import cz.baladee.ecommerce.catalog.domain.repository.ProductRepository
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProductRepositoryImpl(
    private val jpaRepository: ProductJpaRepository
): ProductRepository {
    override fun save(product: Product): Product {
        return jpaRepository.save(product)
    }

    override fun findAll(): List<Product> {
        return jpaRepository.findAll()
    }

    override fun findById(id: UUID): Product {
        return jpaRepository.findById(id)
            .orElseThrow { NotFoundException(Errors.PRODUCT_ID_NOT_FOUND) }
    }
}