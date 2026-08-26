package cz.baladee.ecommerce.user.application.user

import cz.baladee.ecommerce.shared.advice.exception.NotFoundException
import cz.baladee.ecommerce.shared.util.Errors
import cz.baladee.ecommerce.user.application.mapper.UserMapper
import cz.baladee.ecommerce.user.application.user.dto.User as UserDto
import cz.baladee.ecommerce.user.domain.model.User as DomainUser
import cz.baladee.ecommerce.user.domain.repository.UserRepository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val mapper = mockk<UserMapper>()
    private val service = UserService(
        userRepo = userRepository,
        mapper = mapper
    )

    @Test
    fun `loadUser should return mapped user when user exists`() {
        val id = UUID.randomUUID()
        val domainUser = DomainUser(
            id = id,
            email = "jan.novak@example.com",
            password = "encoded-password",
            firstName = "Jan",
            lastName = "Novák",
            phone = "+420777123456",
            createdAt = Instant.parse("2024-01-01T10:00:00Z")
        )
        val expectedDto = UserDto(
            id = id,
            email = domainUser.email,
            firstName = domainUser.firstName,
            lastName = domainUser.lastName,
            phone = domainUser.phone,
            createdAt = domainUser.createdAt,
            updatedAt = domainUser.updatedAt,
        )

        every { userRepository.findById(id) } returns domainUser
        every { mapper.toDto(domainUser) } returns expectedDto

        val result = service.loadUser(id)

        assertEquals(expectedDto, result)
        verify(exactly = 1) { userRepository.findById(id) }
        verify(exactly = 1) { mapper.toDto(domainUser) }
    }

    @Test
    fun `loadUser should throw NotFoundException when user does not exist`() {
        val id = UUID.randomUUID()

        every { userRepository.findById(id) } returns null

        val exception = assertThrows(NotFoundException::class.java) {
            service.loadUser(id)
        }

        assertEquals(Errors.USER_ID_NOT_FOUND.code, exception.code)
        verify(exactly = 0) { mapper.toDto(any<DomainUser>()) }
    }

    @Test
    fun `updateUser should modify and save existing user`() {
        val id = UUID.randomUUID()
        val existingUser = DomainUser(
            id = id,
            email = "jan.novak@example.com",
            password = "encoded-password",
            firstName = "Jan",
            lastName = "Novák",
            phone = "+420777123456"
        )
        val incomingUser = UserDto(
            id = id,
            email = existingUser.email,
            firstName = "Janek",
            lastName = "Novotný",
            phone = "+420777654321",
            createdAt = Instant.now(),
            updatedAt = null,
        )

        every { userRepository.findById(id) } returns existingUser
        justRun { mapper.modifyUser(incomingUser, existingUser) }
        every { userRepository.save(existingUser) } returns existingUser

        service.updateUser(id, incomingUser)

        verify(exactly = 1) { userRepository.findById(id) }
        verify(exactly = 1) { mapper.modifyUser(incomingUser, existingUser) }
        verify(exactly = 1) { userRepository.save(existingUser) }
    }

    @Test
    fun `updateUser should throw NotFoundException when user does not exist`() {
        val id = UUID.randomUUID()
        val incomingUser = UserDto(
            id = id,
            email = "jan.novak@example.com",
            firstName = "Jan",
            lastName = "Novák",
            phone = "+420777123456",
            createdAt = Instant.now(),
            updatedAt = null,
        )

        every { userRepository.findById(id) } returns null

        val exception = assertThrows(NotFoundException::class.java) {
            service.updateUser(id, incomingUser)
        }

        assertEquals(Errors.USER_ID_NOT_FOUND.code, exception.code)
        verify(exactly = 0) { userRepository.save(any<DomainUser>()) }
    }
}