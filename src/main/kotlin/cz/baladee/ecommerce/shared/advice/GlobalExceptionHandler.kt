package cz.baladee.ecommerce.shared.advice

import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
    val message: String,
    val code: String
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = exception.message ?: "Resource not found",
                    code = exception.code ?: Errors.UNKNOWN_EXCEPTION.code
                )
            )
    }
}
