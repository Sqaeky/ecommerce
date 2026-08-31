package cz.baladee.ecommerce.catalog.application.api

import cz.baladee.ecommerce.catalog.domain.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductQueryService(
    private val productRepo: ProductRepository
) {

    fun getProductForCart(productId: UUID): ProductCartInfo {
        val product = productRepo.findById(productId)
        if (!product.isActive || product.deletedAt != null) {
            throw IllegalStateException("Product $productId is not available")
        }

        return ProductCartInfo(
            productId = productId,
            name = product.name,
            price = product.price,
            currency = product.currency
        )
    }
}