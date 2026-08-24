package cz.baladee.ecommerce.catalog.application.dto

import java.util.UUID

data class AddCategoryReq(
    val name: String,
    val description: String?,
    val parentId: UUID?
)
data class AddCategoryRes(
    val id: UUID,
    val slug: String
)
