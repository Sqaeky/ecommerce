package cz.baladee.ecommerce.user.application.auth

import org.springframework.security.core.userdetails.UserDetails

interface JwtTokenProvider {
    fun generateToken(userDetails: UserDetails): String
    fun extractUsername(token: String): String?
    fun validateToken(token: String, userDetails: UserDetails): Boolean
}