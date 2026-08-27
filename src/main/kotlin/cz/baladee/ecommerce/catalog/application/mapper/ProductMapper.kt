package cz.baladee.ecommerce.catalog.application.mapper

import cz.baladee.ecommerce.catalog.application.dto.product.Product
import cz.baladee.ecommerce.catalog.application.dto.product.UpdateProductReq
import cz.baladee.ecommerce.catalog.domain.model.Category
import org.springframework.stereotype.Component
import cz.baladee.ecommerce.catalog.domain.model.Product as DbProduct

@Component
class ProductMapper {

    fun toDto(product: DbProduct): Product {
        return Product(
            id = product.id,
            name = product.name,
            slug = product.slug,
            description = product.description,
            price = product.price,
            currency = product.currency,
            categoryId = product.category.id,
            isActive = product.isActive,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt,
            deletedAt = product.deletedAt
        )
    }

    fun updateProduct(dto: UpdateProductReq, product: DbProduct, category: Category?) {
        product.name = dto.name ?: product.name
        product.description = dto.description ?: product.description
        product.price = dto.price ?: product.price
        product.isActive = dto.isActive ?: product.isActive
        product.category = category ?: product.category
    }
}