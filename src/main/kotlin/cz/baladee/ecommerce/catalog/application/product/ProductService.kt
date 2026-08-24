package cz.baladee.ecommerce.catalog.application.product

import cz.baladee.ecommerce.catalog.application.dto.AddProductReq
import cz.baladee.ecommerce.catalog.application.dto.AddProductRes
import cz.baladee.ecommerce.catalog.domain.model.Product
import cz.baladee.ecommerce.catalog.domain.repository.CategoryRepository
import cz.baladee.ecommerce.catalog.domain.repository.ProductRepository
import cz.baladee.ecommerce.shared.util.toSlug
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(
    private val categoryRepo: CategoryRepository,
    private val productRepo: ProductRepository
) {
    @Transactional
    fun addProduct(req: AddProductReq): AddProductRes {
        val category = categoryRepo.findById(req.categoryId) ?: throw IllegalArgumentException("Category not found")

        if (req.price <= BigDecimal.ZERO) throw IllegalArgumentException("Price must be greater than zero")

        val product = Product(
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
}