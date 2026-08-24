package cz.baladee.ecommerce.catalog.adapter.`in`

import cz.baladee.ecommerce.catalog.application.dto.AddProductReq
import cz.baladee.ecommerce.catalog.application.dto.AddProductRes
import cz.baladee.ecommerce.catalog.application.product.ProductService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/catalog")
class ProductController(
    private val service: ProductService
) {
    @PostMapping("/product")
    fun addProduct(@RequestBody @Valid req: AddProductReq): AddProductRes {
        return service.addProduct(req)
    }
}