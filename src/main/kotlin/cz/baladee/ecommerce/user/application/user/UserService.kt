package cz.baladee.ecommerce.user.application.user

import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors.USER_ID_NOT_FOUND
import cz.baladee.ecommerce.user.application.user.dto.User
import cz.baladee.ecommerce.user.application.user.dto.modifyDbUser
import cz.baladee.ecommerce.user.application.user.dto.toDto
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepo: UserRepository
) {

    fun loadUser(id: UUID): User {
        val user = userRepo.findById(id) ?: throw NotFoundException(USER_ID_NOT_FOUND)
        return user.toDto()
    }

    fun updateUser(id: UUID, user: User): User {
        val existingUser = userRepo.findById(id) ?: throw NotFoundException(USER_ID_NOT_FOUND)
        val updatedUser = user.modifyDbUser(existingUser)
        return userRepo.save(updatedUser).toDto()
    }
}