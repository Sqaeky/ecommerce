package cz.baladee.ecommerce.shared.event

import java.util.UUID

data class ProductCreatedEvent(
    val id: UUID,
    val initialStock: Int? = 0
)