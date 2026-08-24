package cz.baladee.ecommerce.user.application.auth

import cz.baladee.ecommerce.user.adapter.out.security.JwtService
import cz.baladee.ecommerce.user.application.auth.dto.LoginRequest
import cz.baladee.ecommerce.user.application.auth.dto.RegisterRequest
import cz.baladee.ecommerce.user.domain.model.User
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val jwtService = mockk<JwtService>()
    private val authenticationManager = mockk<AuthenticationManager>()

    private val service = AuthService(
        userRepository = userRepository,
        passwordEncoder = passwordEncoder,
        jwtService = jwtService,
        authenticationManager = authenticationManager
    )

    @Test
    fun `register should create user with encoded password and return jwt token`() {
        val req = RegisterRequest(
            email = "jan.novak@example.com",
            password = "secret123",
            firstName = "Jan",
            lastName = "Novák",
            phoneNumber = "+420777123456"
        )
        val encodedPassword = "encoded-secret"
        val savedUser = User(
            email = req.email,
            password = encodedPassword,
            firstName = req.firstName,
            lastName = req.lastName,
            phone = req.phoneNumber
        )

        every { userRepository.existsByEmail(req.email) } returns false
        every { passwordEncoder.encode(req.password) } returns encodedPassword
        every { userRepository.save(any()) } returns savedUser
        every { jwtService.generateToken(any()) } returns "jwt-token"

        val response = service.register(req)

        assertEquals("jwt-token", response.token)
        assertEquals("Bearer", response.type)

        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { jwtService.generateToken(any()) }
    }

    @Test
    fun `register should reject duplicate email`() {
        val req = RegisterRequest(
            email = "jan.novak@example.com",
            password = "secret123"
        )

        every { userRepository.existsByEmail(req.email) } returns true

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.register(req)
        }

        assertEquals("User with email ${req.email} already exists", exception.message)
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `login should authenticate user and return jwt token`() {
        val req = LoginRequest(
            email = "jan.novak@example.com",
            password = "secret123"
        )
        val user = User(
            email = req.email,
            password = "encoded-secret",
            firstName = "Jan",
            lastName = "Novák"
        )

        every { authenticationManager.authenticate(any()) } returns mockk(relaxed = true)
        every { userRepository.findByEmail(req.email) } returns user
        every { jwtService.generateToken(any()) } returns "login-jwt-token"

        val response = service.login(req)

        assertEquals("login-jwt-token", response.token)
        assertEquals("Bearer", response.type)
        verify(exactly = 1) { authenticationManager.authenticate(any()) }
    }

    @Test
    fun `login should fail when user does not exist`() {
        val req = LoginRequest(
            email = "jan.novak@example.com",
            password = "secret123"
        )

        every { authenticationManager.authenticate(any()) } returns mockk(relaxed = true)
        every { userRepository.findByEmail(req.email) } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.login(req)
        }

        assertEquals("User with email ${req.email} not found", exception.message)
    }
}
