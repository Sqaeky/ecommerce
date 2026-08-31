package cz.baladee.ecommerce.cart.application.dto

import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull
import java.util.UUID

data class AddToCartRequest(
    @field:NotNull
    val productId: UUID,

    @field:Min(1)
    val quantity: Int = 1
)
