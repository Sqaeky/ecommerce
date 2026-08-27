package cz.baladee.ecommerce.catalog.application.dto.category

import java.util.UUID

data class Category(
    val id: UUID,
    val name: String,
    val description: String?,
    val slug: String,
    val parentId: UUID?,
    val children: List<Category> = emptyList()
)