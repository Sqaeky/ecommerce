package cz.baladee.ecommerce.catalog.domain.repository

import cz.baladee.ecommerce.catalog.domain.model.Category
import java.util.UUID

interface CategoryRepository {

    fun findById(id: UUID): Category?

    fun save(category: Category): Category

    fun findAll(): List<Category>
}