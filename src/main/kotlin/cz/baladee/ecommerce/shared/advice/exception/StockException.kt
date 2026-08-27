package cz.baladee.ecommerce.shared.advice.exception

import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

open class StockException(error: Errors? = null, message: String? = null) : RuntimeException(message) {
    val code: String? = error?.code
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NegativeQuantityException(error: Errors? = null, message: String? = null) : StockException(error, message)

@ResponseStatus(HttpStatus.CONFLICT)
class InsufficientStockException(error: Errors? = null, message: String? = null) : StockException(error, message)
