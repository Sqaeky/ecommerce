package cz.baladee.ecommerce.shared.advice

import cz.baladee.ecommerce.shared.advice.exception.InsufficientStockException
import cz.baladee.ecommerce.shared.advice.exception.NegativeQuantityException
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.advice.exception.StockException
import cz.baladee.ecommerce.shared.util.Errors
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class ErrorResponse(
    val message: String,
    val code: String,
    val timestamp: Instant = Instant.now(),
    val details: Map<String, String>? = null
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                message = ex.message ?: "Resource not found",
                code = ex.code ?: Errors.UNKNOWN_EXCEPTION.code
            )
        )
    }

    @ExceptionHandler(InsufficientStockException::class)
    fun handleInsufficientStock(ex: InsufficientStockException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                message = ex.message ?: "Insufficient stock",
                code = ex.code ?: Errors.INSUFFICIENT_QUANTITY.code
            )
        )
    }

    @ExceptionHandler(NegativeQuantityException::class)
    fun handleNegativeQuantity(ex: NegativeQuantityException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = ex.message ?: "Invalid quantity",
                code = ex.code ?: Errors.NEGATIVE_QUANTITY.code
            )
        )
    }

    // fallback for other exceptions related to stock management
    @ExceptionHandler(StockException::class)
    fun handleStock(ex: StockException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = ex.message ?: "Stock error",
                code = ex.code ?: Errors.UNKNOWN_EXCEPTION.code
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "invalid") }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = "Validation failed",
                code = Errors.VALIDATION_FAILED.code,
                details = details
            )
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = ex.message ?: "Bad request",
                code = Errors.UNKNOWN_EXCEPTION.code
            )
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(ex: IllegalStateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                message = ex.message ?: "Conflict",
                code = Errors.UNKNOWN_EXCEPTION.code
            )
        )
    }

    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleOptimisticLock(ex: OptimisticLockingFailureException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                message = "Resource was modified concurrently, please retry",
                code = Errors.OPTIMISTIC_LOCKING_FAILURE.code
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        // TODO log the exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                message = "Unexpected error",
                code = Errors.UNKNOWN_EXCEPTION.code
            )
        )
    }
}
