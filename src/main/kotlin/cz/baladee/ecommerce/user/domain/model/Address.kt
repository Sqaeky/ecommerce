package cz.baladee.ecommerce.user.domain.model

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
import java.util.UUID

@Entity
@Table(name = "addresses", schema = "\"user\"")
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: AddressType = AddressType.SHIPPING,

    @Column(nullable = false)
    var street: String,

    @Column(nullable = false)
    var city: String,

    @Column(name = "zip_code", nullable = false)
    var postalCode: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var country: Country,

    @Column(name = "is_default", nullable = false)
    var isDefault: Boolean = false,
)

enum class AddressType {
    BILLING,
    SHIPPING
}

enum class Country {
    CZ,
    DE
}