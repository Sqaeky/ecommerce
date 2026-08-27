package cz.baladee.ecommerce.inventory.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "stock", schema = "inventory")
class Stock(
    @Id
    var productId: UUID,
    @Column(nullable = false)
    var quantity: Int = 0,
    @Column(nullable = false, name = "reserved_quantity")
    var reservedQuantity: Int = 0,
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now(),
    ) {
    val availableQuantity: Int
        get() = quantity - reservedQuantity
}