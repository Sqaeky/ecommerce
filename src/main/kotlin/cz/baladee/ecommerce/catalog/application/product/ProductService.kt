package cz.baladee.ecommerce.catalog.application.product

import cz.baladee.ecommerce.catalog.application.dto.AddProductReq
import cz.baladee.ecommerce.catalog.application.dto.AddProductRes
import cz.baladee.ecommerce.catalog.application.dto.Product
import cz.baladee.ecommerce.catalog.application.dto.UpdateProductReq
import cz.baladee.ecommerce.catalog.application.mapper.ProductMapper
import cz.baladee.ecommerce.catalog.domain.model.Category
import cz.baladee.ecommerce.catalog.domain.model.Product as DbProduct
import cz.baladee.ecommerce.catalog.domain.repository.CategoryRepository
import cz.baladee.ecommerce.catalog.domain.repository.ProductRepository
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import cz.baladee.ecommerce.shared.util.toSlug
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class ProductService(
    private val categoryRepo: CategoryRepository,
    private val productRepo: ProductRepository,
    private val mapper: ProductMapper
) {
    @Transactional
    fun addProduct(req: AddProductReq): AddProductRes {
        val category = categoryRepo.findById(req.categoryId) ?: throw IllegalArgumentException("Category not found")

        if (req.price <= BigDecimal.ZERO) throw IllegalArgumentException("Price must be greater than zero")

        val product = DbProduct(
            name = req.name,
            slug = req.name.toSlug(),
            description = req.description,
            price = req.price,
            category = category
        )
        val savedProduct = productRepo.save(product)
        val productId = savedProduct.id
            ?: throw IllegalStateException("Saved product ID is null")

        return AddProductRes(
            id = productId,
            slug = savedProduct.slug
        )
    }

    fun getProducts(activeFlag: Boolean): List<Product> {
        val products = productRepo.findAll().map { mapper.toDto(it) }
        if (activeFlag) {
            products.filter { product -> product.isActive }
        }
        return products
    }

    fun getProduct(id: UUID): Product {
        val product = productRepo.findById(id)
        return mapper.toDto(product)
    }

    @Transactional
    fun updateProduct(id: UUID, req: UpdateProductReq) {
        val product = productRepo.findById(id)
        var category: Category? = null
        if (req.categoryId != null) {
            category = categoryRepo.findById(req.categoryId) ?: throw NotFoundException(Errors.CATEGORY_ID_NOT_FOUND)
        }
        mapper.updateProduct(req, product, category)
        productRepo.save(product)
    }

    fun softDeleteProduct(id: UUID) {
        val product = productRepo.findById(id)
        product.isActive = false
        product.deletedAt = Instant.now()
        productRepo.save(product)
    }
}