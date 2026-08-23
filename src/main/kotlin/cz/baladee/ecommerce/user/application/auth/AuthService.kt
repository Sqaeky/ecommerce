package cz.baladee.ecommerce.user.application.auth

import cz.baladee.ecommerce.user.adapter.out.security.CustomUserDetails
import cz.baladee.ecommerce.user.adapter.out.security.JwtService
import cz.baladee.ecommerce.user.application.auth.dto.AuthResponse
import cz.baladee.ecommerce.user.application.auth.dto.LoginRequest
import cz.baladee.ecommerce.user.application.auth.dto.RegisterRequest
import cz.baladee.ecommerce.user.domain.model.User
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(req: RegisterRequest): AuthResponse {
        // Check if user already exists
        if (userRepository.existsByEmail(req.email)) {
            throw IllegalArgumentException("User with email ${req.email} already exists")
        }

        val user = User(
            email = req.email,
            password = encodePassword(req.password),
            firstName = req.firstName,
            lastName = req.lastName,
            phone = req.phoneNumber,
            updatedAt = Instant.now()
        )
        val savedUser = userRepository.save(user)

        val jwtToken = jwtService.generateToken(CustomUserDetails(savedUser))

        return AuthResponse(token = jwtToken)
    }

    fun login(req: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                req.email,
                req.password
            )
        )

        val user = userRepository.findByEmail(req.email)
            ?: throw IllegalArgumentException("User with email ${req.email} not found")

        val jwtToken = jwtService.generateToken(CustomUserDetails(user))

        return AuthResponse(token = jwtToken)
    }

    private fun encodePassword(password: String): String {
        return passwordEncoder.encode(password) ?: throw IllegalArgumentException("Password encoding failed")
    }

}