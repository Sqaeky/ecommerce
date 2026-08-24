package cz.baladee.ecommerce.catalog.application.category

import cz.baladee.ecommerce.catalog.application.dto.AddCategoryReq
import cz.baladee.ecommerce.catalog.application.dto.AddCategoryRes
import cz.baladee.ecommerce.catalog.domain.model.Category
import cz.baladee.ecommerce.catalog.domain.repository.CategoryRepository
import cz.baladee.ecommerce.shared.util.toSlug
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val repository: CategoryRepository
) {

    fun addCategory(req: AddCategoryReq): AddCategoryRes {
        val category = Category(
            name = req.name,
            slug = req.name.toSlug(),
            description = req.description
        )
        val savedCategory = repository.save(category)

        return AddCategoryRes(
            id = savedCategory.id!!,
            slug = savedCategory.slug
        )
    }
}