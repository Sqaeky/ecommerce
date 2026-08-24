package cz.baladee.ecommerce.catalog.domain.repository

import cz.baladee.ecommerce.catalog.domain.model.Product

interface ProductRepository {

    fun save(product: Product): Product
}