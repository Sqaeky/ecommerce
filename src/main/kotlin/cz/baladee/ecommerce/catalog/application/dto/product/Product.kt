package cz.baladee.ecommerce.catalog.application.dto.product

import cz.baladee.ecommerce.catalog.domain.model.Currency
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Product(
    val id: UUID?,
    val name: String,
    val slug: String,
    val description: String?,
    val price: BigDecimal,
    val currency: Currency,
    val categoryId: UUID?,
    val isActive: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
)