package cz.baladee.ecommerce.user.adapter.out.security

import cz.baladee.ecommerce.user.domain.model.User
import cz.baladee.ecommerce.user.domain.model.UserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    private val user: User
): UserDetails {
    override fun getUsername(): String = user.email

    override fun getPassword(): String = user.password

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean {
        return user.status == UserStatus.ACTIVE && user.deletedAt == null
    }

    fun getId(): UUID = user.id!!
    fun getUser(): User = user
}