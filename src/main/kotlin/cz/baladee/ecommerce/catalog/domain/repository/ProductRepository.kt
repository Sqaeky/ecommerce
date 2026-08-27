package cz.baladee.ecommerce.catalog.domain.repository

import cz.baladee.ecommerce.catalog.domain.model.Product
import java.util.UUID

interface ProductRepository {

    fun save(product: Product): Product

    fun findAll(): List<Product>

    fun findById(id: UUID): Product
}