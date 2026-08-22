package cz.baladee.ecommerce.user.adapter.out.persistence

import cz.baladee.ecommerce.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}