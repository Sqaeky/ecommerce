package cz.baladee.ecommerce.user.domain.repository

import cz.baladee.ecommerce.user.domain.model.User
import java.util.UUID

interface UserRepository {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    fun save(user: User): User

    fun findById(id: UUID): User?
}