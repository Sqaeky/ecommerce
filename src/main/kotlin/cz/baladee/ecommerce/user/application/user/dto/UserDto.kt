package cz.baladee.ecommerce.user.application.user.dto

import cz.baladee.ecommerce.user.domain.model.AddressType
import cz.baladee.ecommerce.user.domain.model.Country
import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID? = null,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val addresses: List<Address> = emptyList()
)

data class Address(
    val id: UUID? = null,
    val type: AddressType,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: Country,
)
