package cz.baladee.ecommerce.catalog.application.dto.product

import cz.baladee.ecommerce.catalog.domain.model.Currency
import java.math.BigDecimal
import java.util.UUID

data class ProductCartInfo(
    val productId: UUID,
    val name: String,
    val price: BigDecimal,
    val currency: Currency
)
