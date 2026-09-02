package cz.baladee.ecommerce.user.adapter.out.security

import cz.baladee.ecommerce.user.application.auth.JwtTokenProvider
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(
    @Value("\${spring.jwt.secret}")
    private val secret: String,
    @Value("\${spring.jwt.expiration-ms:84600000}") // Default to 24 hours if not set
    private val expirationMs: Long
): JwtTokenProvider {

    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    override fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiration = Date(now.time + expirationMs)

        return Jwts.builder()
            .claim(Claims.SUBJECT, userDetails.username)
            .claim(Claims.ISSUED_AT, now)
            .claim(Claims.EXPIRATION, expiration)
            .signWith(key)
            .compact()
    }

    override fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    override fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

}