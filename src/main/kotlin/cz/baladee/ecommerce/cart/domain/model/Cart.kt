package cz.baladee.ecommerce.cart.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.time.Instant
import java.util.UUID

@Entity
class Cart(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "user_id")
    var userId: UUID? = null,

    @Column(name = "session_id")
    var sessionId: String? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<CartItem> = mutableListOf(),

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @Column(name = "expires_at")
    var expiresAt: Instant? = null,
)