package cz.baladee.ecommerce.catalog.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "products", schema = "catalog")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(unique = true, nullable = false)
    var slug: String,       // Url-friendly version of the name

    var description: String? = null,

    @Column(nullable = false)
    var price: BigDecimal,

    @Enumerated(EnumType.STRING)
    var currency: Currency = Currency.CZK,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false, name = "created_at")
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false, name = "updated_at")
    var updatedAt: Instant = Instant.now(),

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
)

enum class Currency {
    EUR,
    CZK
}