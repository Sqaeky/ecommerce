package cz.baladee.ecommerce.user.application.user

import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors.USER_ID_NOT_FOUND
import cz.baladee.ecommerce.user.application.mapper.UserMapper
import cz.baladee.ecommerce.user.application.user.dto.UpdateUserReq
import cz.baladee.ecommerce.user.application.user.dto.User
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepo: UserRepository,
    private val mapper: UserMapper
) {

    fun loadUser(id: UUID): User {
        val user = userRepo.findById(id) ?: throw NotFoundException(USER_ID_NOT_FOUND)
        return mapper.toDto(user)
    }

    fun updateUser(id: UUID, user: UpdateUserReq) {
        val existingUser = userRepo.findById(id) ?: throw NotFoundException(USER_ID_NOT_FOUND)
        mapper.modifyUser(user, existingUser)
        userRepo.save(existingUser)
    }
}