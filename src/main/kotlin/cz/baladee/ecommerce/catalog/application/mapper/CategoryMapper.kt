package cz.baladee.ecommerce.catalog.application.mapper

import cz.baladee.ecommerce.catalog.application.dto.category.Category
import org.springframework.stereotype.Component
import cz.baladee.ecommerce.catalog.domain.model.Category as DbCategory

@Component
class CategoryMapper {

    fun toDto(dbCategory: DbCategory): Category {
        return Category(
            id = dbCategory.id!!,
            name = dbCategory.name,
            description = dbCategory.description,
            slug = dbCategory.slug,
            parentId = dbCategory.parent?.id,
            children = dbCategory.children.map { toDto(it) }
        )
    }
}