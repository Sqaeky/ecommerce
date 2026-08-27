package cz.baladee.ecommerce

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.modulith.core.ApplicationModules

@SpringBootTest
class EcommerceApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun verifyModularity() {
		ApplicationModules.of(EcommerceApplication::class.java).verify()
	}

}
