package cz.baladee.ecommerce.shared

import cz.baladee.ecommerce.shared.advice.GlobalExceptionHandler
import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

private const val USER_ID = 1L

class GlobalExceptionHandlerTest {

    private val exceptionHandler = GlobalExceptionHandler()

    @Test
    fun `handleNotFoundException returns 404 with formatted error response`() {
        val exception = NotFoundException(
            Errors.USER_ID_NOT_FOUND,
            "User with id $USER_ID was not found"
        )

        val response = exceptionHandler.handleNotFoundException(exception)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("User with id $USER_ID was not found", response.body?.message)
        assertEquals("001", response.body?.code)
    }
}