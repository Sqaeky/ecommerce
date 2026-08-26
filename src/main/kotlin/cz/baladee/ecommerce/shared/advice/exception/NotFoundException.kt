package cz.baladee.ecommerce.shared.advice.exception

import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(error: Errors? = null, message: String? = null) : RuntimeException(message) {
    val code: String? = error?.code
}
