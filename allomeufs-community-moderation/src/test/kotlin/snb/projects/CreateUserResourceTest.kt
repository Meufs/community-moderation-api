package snb.projects

import com.fasterxml.jackson.databind.ObjectMapper
import snb.projects.application.controllers.CookieUtils
import com.templates.domain.errors.ApplicationException
import snb.projects.domain.errors.ApplicationExceptionsEnum
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.ws.rs.core.NewCookie
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import snb.projects.application.dto.requests.CreateAdminRequest
import snb.projects.application.dto.responses.CreateUserResponse
import snb.projects.application.mappers.UsersDtoMappers
import snb.projects.domain.models.commands.users.CreateUserCommand
import snb.projects.domain.models.users.UserBasicInformations
import snb.projects.domain.models.users.UserTypes
import snb.projects.domain.ports.`in`.CreateUsersIn
import snb.projects.domain.ports.`in`.CsrfTokenGeneratorIn
import snb.projects.domain.services.JwtTokenGenerator

@QuarkusTest
@DisplayName("Create users endpoint")
@ExtendWith(MockitoExtension::class)
class CreateUserResourceTest {
    @InjectMock
    private lateinit var csrfTokenGeneratorIn: CsrfTokenGeneratorIn

    @InjectMock
    private lateinit var cookieUtils: CookieUtils

    @Inject
    private lateinit var jwtTokenGenerator: JwtTokenGenerator

    private val usersDtoMappers: UsersDtoMappers = Mockito.mock(UsersDtoMappers::class.java)

    @InjectMock
    private lateinit var createUsersIn: CreateUsersIn

    companion object {
        val adminRequest: CreateAdminRequest = CreateAdminRequest(
            "Sid",
            "Bennaceur",
            "test@test.com",
            "123S0leil!",
            "0613511351",
            "123456789"
        )
        val mappedRequest: CreateUserCommand = CreateUserCommand(
            adminRequest.firstName,
            adminRequest.lastName,
            adminRequest.mail,
            adminRequest.password,
            adminRequest.phoneNumber,
            null,
            null,
            null,
            null
        )
        private val mapper = ObjectMapper()
        private val json: String = mapper.writeValueAsString(adminRequest)
    }

    @Test
    @DisplayName("Should create admin")
    fun testCreateAdminOkRequest() {
        val csrfToken = "ATOKEN"
        val jwtToken = jwtTokenGenerator.getToken(adminRequest.mail, UserTypes.ADMIN.name)
        val userBasicInformations = UserBasicInformations(
            UserTypes.ADMIN.name,
            "ABCDEF",
            jwtToken,
            false
        )
        val createUserCommandCaptor = argumentCaptor<CreateUserCommand>()
        val adminCodeCaptor = argumentCaptor<String>()

        whenever(usersDtoMappers.fromCreationRequest(adminRequest)).thenReturn(mappedRequest)
        whenever(
            createUsersIn.createAdmin(
                any(), any()
            )
        ).thenReturn(userBasicInformations)
        whenever(cookieUtils.setUpCookie("Bearer", userBasicInformations.jwToken))
            .thenReturn(
                NewCookie.Builder("Bearer").value(jwtToken).maxAge(64800).httpOnly(true).path("/")
                    .build()
            )
        whenever(csrfTokenGeneratorIn.generateToken(adminRequest.mail)).thenReturn(csrfToken)
        whenever(cookieUtils.setUpCookie("csrf-token", csrfToken)).thenReturn(
            NewCookie.Builder("csrf-token").value
                (csrfToken).maxAge(64800).httpOnly(false).path("/").build()
        )

        val res = RestAssured.given()
            .accept(ContentType.JSON)
            .header("Content-Type", "application/json")
            .body(json)
            .`when`()
            .post("/users-create/admin")
            .then().extract()

        verify(createUsersIn).createAdmin(createUserCommandCaptor.capture(), adminCodeCaptor.capture())

        commonAsserts(createUserCommandCaptor, adminCodeCaptor)
        val responseBody = Json.decodeFromString<CreateUserResponse>(res.body().asString())
        assertTrue(responseBody.type == UserTypes.ADMIN.name)
        assertTrue(responseBody.reference == userBasicInformations.reference)
        assertEquals(res.statusCode(), 200)
        assertEquals(res.cookie("csrf-token"), csrfToken)
        assertEquals(res.cookie("Bearer"), jwtToken)

    }

    @Test
    @DisplayName("Should get bad request")
    fun testCreateAdminBadRequest() {
        val createUserCommandCaptor = argumentCaptor<CreateUserCommand>()
        val adminCodeCaptor = argumentCaptor<String>()

        whenever(usersDtoMappers.fromCreationRequest(adminRequest)).thenReturn(mappedRequest)
        whenever(
            createUsersIn.createAdmin(
                any(), any()
            )
        ).thenThrow(ApplicationException(ApplicationExceptionsEnum.CREATE_USER_INVALID_PHONE_NUMBER))

        val res = RestAssured.given()
            .accept(ContentType.JSON)
            .header("Content-Type", "application/json")
            .body(json)
            .`when`()
            .post("/users-create/admin")
            .then().extract()

        verify(createUsersIn).createAdmin(createUserCommandCaptor.capture(), adminCodeCaptor.capture())

        commonAsserts(createUserCommandCaptor, adminCodeCaptor)
        assertEquals(res.statusCode(), 400)
        val responseBody = res.body().`as`(ApplicationException::class.java)
        assertTrue(responseBody.origin == ApplicationExceptionsEnum.CREATE_USER_INVALID_PHONE_NUMBER.origin)
        assertTrue(responseBody.message == ApplicationExceptionsEnum.CREATE_USER_INVALID_PHONE_NUMBER.message)
    }

    private fun commonAsserts(
        createUserCommandCaptor: KArgumentCaptor<CreateUserCommand>,
        adminCodeCaptor: KArgumentCaptor<String>
    ) {
        assertTrue(createUserCommandCaptor.firstValue.mail == adminRequest.mail)
        assertTrue(createUserCommandCaptor.firstValue.firstName == adminRequest.firstName)
        assertTrue(createUserCommandCaptor.firstValue.lastName == adminRequest.lastName)
        assertTrue(createUserCommandCaptor.firstValue.password == adminRequest.password)
        assertTrue(createUserCommandCaptor.firstValue.phoneNumber == adminRequest.phoneNumber)
        assertTrue(adminCodeCaptor.firstValue == adminRequest.adminCode)
    }


}