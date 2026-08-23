package cz.baladee.ecommerce.user.adapter.`in`

import cz.baladee.ecommerce.user.application.auth.AuthService
import cz.baladee.ecommerce.user.application.auth.dto.AuthResponse
import cz.baladee.ecommerce.user.application.auth.dto.LoginRequest
import cz.baladee.ecommerce.user.application.auth.dto.RegisterRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val service: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid req: RegisterRequest): ResponseEntity<AuthResponse> {
        val res = service.register(req)
        return ResponseEntity.status(HttpStatus.CREATED).body(res)
    }
    @PostMapping("/login")
    fun login(@RequestBody @Valid req: LoginRequest): ResponseEntity<AuthResponse> {
        val res = service.login(req)
        return ResponseEntity.ok(res)
    }
}