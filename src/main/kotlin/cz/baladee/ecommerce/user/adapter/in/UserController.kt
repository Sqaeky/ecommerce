package cz.baladee.ecommerce.user.adapter.`in`

import cz.baladee.ecommerce.user.application.user.UserService
import cz.baladee.ecommerce.user.application.user.dto.User
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

    @GetMapping("/{id}")
    fun loadUser(@PathVariable id: UUID): User {
        return service.loadUser(id)
    }

    @PutMapping("/update/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody user: User): User {
        return service.updateUser(id, user)
    }
}