package cz.baladee.ecommerce.shared

import cz.baladee.ecommerce.shared.util.toSlug
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ServiceUtilsTest {

    @Test
    fun `toSlug normalizes accents and whitespace`() {
        val slug = "Café / Můj svět!".toSlug()

        assertEquals("cafe-muj-svet", slug)
    }

    @Test
    fun `toSlug compresses repeated separators`() {
        val slug = "  Hello   /   World  !!!  ".toSlug()

        assertEquals("hello-world", slug)
    }
}