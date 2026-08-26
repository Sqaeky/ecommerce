package cz.baladee.ecommerce.user.application.user.dto

import cz.baladee.ecommerce.user.domain.model.AddressType
import cz.baladee.ecommerce.user.domain.model.Address as DbAddress
import cz.baladee.ecommerce.user.domain.model.User as DbUser
import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val addresses: List<Address> = emptyList()
)

data class Address(
    val id: UUID? = null,
    val type: AddressType,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String
)

fun DbUser.toDto(): User {
    return User(
        id = this.id!!,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        phone = this.phone,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        addresses = this.addresses.map { it.toDto() }
    )
}

fun DbAddress.toDto(): Address {
    return Address(
        id = this.id,
        type = this.type,
        street = this.street,
        city = this.city,
        postalCode = this.postalCode,
        country = this.country
    )
}

fun User.modifyDbUser(existingUser: DbUser): DbUser {
    existingUser.firstName = this.firstName ?: existingUser.firstName
    existingUser.lastName = this.lastName ?: existingUser.lastName
    existingUser.phone = this.phone ?: existingUser.phone
    existingUser.updatedAt = Instant.now()
    return existingUser
}
