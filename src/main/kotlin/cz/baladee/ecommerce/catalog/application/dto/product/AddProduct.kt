package cz.baladee.ecommerce.catalog.application.dto.product

import java.math.BigDecimal
import java.util.UUID

data class AddProductReq(
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val categoryId: UUID
)

data class AddProductRes(
    val id: UUID,
    val slug: String
)