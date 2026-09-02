package cz.baladee.ecommerce.catalog.adapter.`in`

import cz.baladee.ecommerce.catalog.application.CategoryService
import cz.baladee.ecommerce.catalog.application.dto.category.AddCategoryReq
import cz.baladee.ecommerce.catalog.application.dto.category.Category
import cz.baladee.ecommerce.catalog.application.dto.category.UpdateCategoryReq
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/catalog")
class CategoryController(
    private val service: CategoryService
) {

    @PostMapping("/category")
    fun addCategory(@RequestBody @Valid req: AddCategoryReq): Category {
        return service.addCategory(req)
    }

    @GetMapping("/categories")
    fun getCategories(): List<Category> = service.getCategories()


    @GetMapping("/category/{id}")
    fun getCategory(@PathVariable id: UUID): Category = service.getCategory(id)

    @PutMapping("/category/{id}/update")
    fun updateCategory(@PathVariable id: UUID, @RequestBody req: UpdateCategoryReq) = service.updateCategory(id, req)

    // TODO dodělat delete
}