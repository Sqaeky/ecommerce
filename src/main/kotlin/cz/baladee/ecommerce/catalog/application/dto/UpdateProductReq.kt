package cz.baladee.ecommerce.catalog.application.dto

import java.math.BigDecimal
import java.util.UUID

data class UpdateProductReq(
    val name: String? = null,
    val description: String? = null,
    val price: BigDecimal? = null,
    val categoryId: UUID? = null,
    val isActive: Boolean? = null
)