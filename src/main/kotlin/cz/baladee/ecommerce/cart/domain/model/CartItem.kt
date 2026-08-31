package cz.baladee.ecommerce.cart.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(schema = "cart", name = "cart_items")
class CartItem(

    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    var cart: Cart? = null,

    @Column(name = "product_id", nullable = false)
    var productId: UUID,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(name = "price_at_addition", nullable = false, precision = 12, scale = 2)
    var priceAtAddition: BigDecimal,

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
    )