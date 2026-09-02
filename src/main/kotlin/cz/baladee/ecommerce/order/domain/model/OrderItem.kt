package cz.baladee.ecommerce.order.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(schema = "\"order\"", name = "order_items")
class OrderItem(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order,

    var productId: UUID,
    var productName: String,
    var quantity: Int,
    var unitPrice: BigDecimal,
    var totalPrice: BigDecimal
)