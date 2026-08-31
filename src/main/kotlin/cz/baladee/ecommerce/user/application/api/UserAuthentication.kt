package cz.baladee.ecommerce.user.application.api

import cz.baladee.ecommerce.user.adapter.out.security.CustomUserDetails
import org.springframework.security.core.Authentication
import java.util.UUID

object UserAuthentication {
    fun userId(authentication: Authentication): UUID {
        val principal = authentication.principal as CustomUserDetails
        return principal.getId()
    }
}