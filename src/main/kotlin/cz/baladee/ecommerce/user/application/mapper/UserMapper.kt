package cz.baladee.ecommerce.user.application.mapper

import cz.baladee.ecommerce.user.application.user.dto.Address
import cz.baladee.ecommerce.user.application.user.dto.User
import cz.baladee.ecommerce.user.domain.model.Address as DbAddress
import cz.baladee.ecommerce.user.domain.model.User as DbUser
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UserMapper {

    fun toDto(dbUser: DbUser): User {
        return User(
            id = dbUser.id,
            email = dbUser.email,
            firstName = dbUser.firstName,
            lastName = dbUser.lastName,
            phone = dbUser.phone,
            createdAt = dbUser.createdAt,
            updatedAt = dbUser.updatedAt,
            addresses = dbUser.addresses.map { toDto(it) }
        )
    }

    fun toDto(dbAddress: DbAddress): Address {
        return Address(
            id = dbAddress.id,
            type = dbAddress.type,
            street = dbAddress.street,
            city = dbAddress.city,
            postalCode = dbAddress.postalCode,
            country = dbAddress.country
        )
    }

    fun modifyUser(dto: User, existingUser: DbUser) {
        existingUser.firstName = dto.firstName ?: existingUser.firstName
        existingUser.lastName = dto.lastName ?: existingUser.lastName
        existingUser.phone = dto.phone ?: existingUser.phone
        existingUser.updatedAt = Instant.now()

        if (dto.addresses.isNotEmpty()) {
            modifyAddresses(dto.addresses, existingUser)
        }
    }

    fun modifyAddresses(dtos: List<Address>, user: DbUser) {
        dtos.forEach { dto ->
            val existingAddress = user.addresses.find { it.type == dto.type }

            if (existingAddress != null) {
                existingAddress.street = dto.street
                existingAddress.city = dto.city
                existingAddress.postalCode = dto.postalCode
                existingAddress.country = dto.country
            } else {
                val newAddress = DbAddress(
                    type = dto.type,
                    street = dto.street,
                    city = dto.city,
                    postalCode = dto.postalCode,
                    country = dto.country,
                    user = user
                )
                user.addresses.add(newAddress)
            }
        }
    }
}