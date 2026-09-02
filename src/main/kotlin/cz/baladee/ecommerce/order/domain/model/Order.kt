package cz.baladee.ecommerce.order.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(schema = "\"order\"", name = "orders")
class Order(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false)
    var orderNumber: String,

    @Column(nullable = false)
    var userId: UUID,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<OrderItem> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.CREATED,

    @Column(name = "total_price", nullable = false)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, length = 3)
    var currency: String = "CZK",

    @Column(name = "shipping_address_id")
    var shippingAddressId: UUID,

    @Column(name = "billing_address_id")
    var billingAddressId: UUID,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Column(name = "finished_at")
    var finishedAt: Instant? = null,

    @Column(nullable = false)
    var version: Int = 1
)

enum class Status {
    CREATED,
    PAID,
    PENDING,
    SHIPPED,
    PROCESSING,
    DELIVERED,
    CANCELLED
}