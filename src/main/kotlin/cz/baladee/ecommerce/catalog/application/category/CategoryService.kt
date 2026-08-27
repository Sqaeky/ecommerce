package cz.baladee.ecommerce.catalog.application.category

import cz.baladee.ecommerce.catalog.application.dto.category.AddCategoryReq
import cz.baladee.ecommerce.catalog.application.dto.category.Category
import cz.baladee.ecommerce.catalog.application.dto.category.UpdateCategoryReq
import cz.baladee.ecommerce.catalog.application.mapper.CategoryMapper
import cz.baladee.ecommerce.catalog.domain.model.Category as DbCategory
import cz.baladee.ecommerce.catalog.domain.repository.CategoryRepository
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import cz.baladee.ecommerce.shared.util.toSlug
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
    private val repo: CategoryRepository,
    private val mapper: CategoryMapper
) {

    fun addCategory(req: AddCategoryReq): Category {
        val category = DbCategory(
            name = req.name,
            slug = req.name.toSlug(),
            description = req.description
        )
        val savedCategory = repo.save(category)

        return mapper.toDto(savedCategory)
    }

    fun getCategories(): List<Category> {
        return repo.findAll().map { mapper.toDto(it) }
    }

    fun getCategory(id: UUID): Category {
        return mapper.toDto(repo.findById(id)
            ?: throw NotFoundException(Errors.CATEGORY_ID_NOT_FOUND))
    }

    // TODO dodělat úpravu i podkategorií
    @Transactional
    fun updateCategory(id: UUID, req: UpdateCategoryReq) {
        val category = repo.findById(id) ?: throw NotFoundException(Errors.CATEGORY_ID_NOT_FOUND)
        category.name = req.name ?: category.name
        category.description = req.description ?: category.description
        repo.save(category)
    }
}