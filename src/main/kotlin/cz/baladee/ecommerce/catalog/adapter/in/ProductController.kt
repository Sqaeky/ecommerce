package cz.baladee.ecommerce.catalog.adapter.`in`

import cz.baladee.ecommerce.catalog.application.dto.AddProductReq
import cz.baladee.ecommerce.catalog.application.dto.AddProductRes
import cz.baladee.ecommerce.catalog.application.dto.Product
import cz.baladee.ecommerce.catalog.application.dto.UpdateProductReq
import cz.baladee.ecommerce.catalog.application.product.ProductService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/catalog")
class ProductController(
    private val service: ProductService
) {
    @PostMapping("/product")
    fun addProduct(@RequestBody @Valid req: AddProductReq): AddProductRes {
        return service.addProduct(req)
    }

    // returns products based on activeFlag (true = only active products)
    @GetMapping("/products")
    fun getProducts(
        @RequestParam(required = false, defaultValue = "true") activeFlag: Boolean
    ): List<Product> {
        return service.getProducts(activeFlag)
    }

    @GetMapping("/product/{id}")
    fun getProduct(@PathVariable id: UUID): Product {
        return service.getProduct(id)
    }

    @PutMapping("/product/{id}/update")
    fun updateProduct(@PathVariable id: UUID, @RequestBody product: UpdateProductReq) {
        service.updateProduct(id, product)
    }

    @DeleteMapping("/product/{id}/delete")
    fun deleteProduct(@PathVariable id: UUID) = service.softDeleteProduct(id)
}