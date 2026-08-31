package cz.baladee.ecommerce

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.modulith.core.ApplicationModules

@SpringBootTest
class EcommerceApplicationTests {

    @TestConfiguration
    class EventPublicationRepoTestConfig {
        @Bean
        @Primary
        fun eventPublicationRepository(@Qualifier("jpaEventPublicationRepository") jpa: Any): Any {
            return jpa
        }
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun verifyModularity() {
        ApplicationModules.of(EcommerceApplication::class.java).verify()
    }

}
