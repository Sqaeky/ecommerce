package cz.baladee.ecommerce.user.adapter.`in`

import cz.baladee.ecommerce.user.adapter.out.security.CustomUserDetails
import cz.baladee.ecommerce.user.application.user.UserService
import cz.baladee.ecommerce.user.application.user.dto.UpdateUserReq
import cz.baladee.ecommerce.user.application.user.dto.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    private val service: UserService
) {

    @GetMapping("/me")
    fun loadCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication?.principal as CustomUserDetails
        return service.loadUser(userDetails.getId())
    }

    @PutMapping("/update/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody user: UpdateUserReq) {
        service.updateUser(id, user)
    }
}