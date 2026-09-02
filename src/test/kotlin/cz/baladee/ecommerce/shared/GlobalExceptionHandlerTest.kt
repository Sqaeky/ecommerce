package cz.baladee.ecommerce.shared

import cz.baladee.ecommerce.shared.advice.GlobalExceptionHandler
import cz.baladee.ecommerce.shared.advice.exception.InsufficientStockException
import cz.baladee.ecommerce.shared.advice.exception.NegativeQuantityException
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus
import java.util.UUID

class GlobalExceptionHandlerTest {

    private val exceptionHandler = GlobalExceptionHandler()

    private companion object {
        val USER_ID: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val PRODUCT_ID: UUID = UUID.fromString("22222222-2222-2222-2222-222222222222")
    }

    @Test
    fun `handleNotFoundException returns 404 with formatted error response`() {
        val exception = NotFoundException(
            Errors.USER_ID_NOT_FOUND,
            "User with id $USER_ID was not found"
        )

        val response = exceptionHandler.handleNotFoundException(exception)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("User with id $USER_ID was not found", response.body!!.message)
        assertEquals("001", response.body!!.code)
        assertNotNull(response.body!!.timestamp)
        assertNull(response.body!!.details)
    }

    @Test
    fun `handleNotFoundException uses default message and unknown code when missing`() {
        val exception = NotFoundException()

        val response = exceptionHandler.handleNotFoundException(exception)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Resource not found", response.body!!.message)
        assertEquals(Errors.UNKNOWN_EXCEPTION.code, response.body!!.code)
    }

    @Test
    fun `handleInsufficientStock returns 409`() {
        val exception = InsufficientStockException(
            Errors.INSUFFICIENT_QUANTITY,
            "Not enough stock for product $PRODUCT_ID"
        )

        val response = exceptionHandler.handleInsufficientStock(exception)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Not enough stock for product $PRODUCT_ID", response.body!!.message)
        assertEquals("005", response.body!!.code)
    }

    @Test
    fun `handleNegativeQuantity returns 400`() {
        val exception = NegativeQuantityException(
            Errors.NEGATIVE_QUANTITY,
            "Reserve quantity cannot be negative number"
        )

        val response = exceptionHandler.handleNegativeQuantity(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Reserve quantity cannot be negative number", response.body!!.message)
        assertEquals("004", response.body!!.code)
    }

    @Test
    fun `handleIllegalArgument returns 400`() {
        val exception = IllegalArgumentException("User with email test@example.com already exists")

        val response = exceptionHandler.handleIllegalArgument(exception)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("User with email test@example.com already exists", response.body!!.message)
        assertEquals(Errors.UNKNOWN_EXCEPTION.code, response.body!!.code)
    }

    @Test
    fun `handleIllegalState returns 409`() {
        val exception = IllegalStateException("Cart is empty")

        val response = exceptionHandler.handleIllegalState(exception)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Cart is empty", response.body!!.message)
    }

    @Test
    fun `handleOptimisticLock returns 409`() {
        val exception = OptimisticLockingFailureException("Version conflict")

        val response = exceptionHandler.handleOptimisticLock(exception)

        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Resource was modified concurrently, please retry", response.body!!.message)
    }

    @Test
    fun `handleGeneric returns 500 without leaking internal message`() {
        val exception = RuntimeException("Sensitive DB connection string")

        val response = exceptionHandler.handleGeneric(exception)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("Unexpected error", response.body!!.message)
        assertEquals(Errors.UNKNOWN_EXCEPTION.code, response.body!!.code)
        assertTrue(response.body!!.message.contains("Sensitive").not())
    }
}