package cz.baladee.ecommerce.user.adapter.out.persistence

import cz.baladee.ecommerce.user.domain.model.User
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val jpaRepo: UserJpaRepository
): UserRepository {
    override fun findByEmail(email: String): User? {
        return jpaRepo.findByEmail(email)
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepo.existsByEmail(email)
    }

    override fun save(user: User): User {
        return jpaRepo.save(user)
    }

    override fun findById(id: UUID): User? {
        return jpaRepo.findById(id).orElse(null)
    }
}