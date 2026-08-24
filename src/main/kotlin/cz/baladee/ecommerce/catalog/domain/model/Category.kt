package cz.baladee.ecommerce.catalog.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(schema = "catalog", name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    var name: String,

    @Column(unique = true, nullable = false)
    var slug: String, // Url-friendly version of the name

    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL])
    var children: MutableList<Category> = mutableListOf(),

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    var products: MutableList<Product> = mutableListOf(),

    @Column(nullable = false, name = "created_at")
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false, name = "updated_at")
    var updatedAt: Instant = Instant.now()
)