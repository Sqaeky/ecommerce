package cz.baladee.ecommerce.catalog.adapter.`in`

import cz.baladee.ecommerce.catalog.application.category.CategoryService
import cz.baladee.ecommerce.catalog.application.dto.AddCategoryReq
import cz.baladee.ecommerce.catalog.application.dto.AddCategoryRes
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/catalog")
class CategoryController(
    private val service: CategoryService
) {

    @PostMapping("/category")
    fun addCategory(@RequestBody @Valid req: AddCategoryReq): AddCategoryRes {
        return service.addCategory(req)
    }
}