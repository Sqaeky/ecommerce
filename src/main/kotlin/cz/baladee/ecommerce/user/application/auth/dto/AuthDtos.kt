package cz.baladee.ecommerce.user.application.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank("Email is required")
    @field:Email("Email is not valid")
    val email: String,
    @field:NotBlank("Password is required")
    val password: String
)

data class RegisterRequest(
    @field:NotBlank("Email is required")
    @field:Email("Email is not valid")
    val email: String,
    @field:NotBlank("Password is required")
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
)

data class AuthResponse(
    val token: String,
    val type: String = "Bearer"
)