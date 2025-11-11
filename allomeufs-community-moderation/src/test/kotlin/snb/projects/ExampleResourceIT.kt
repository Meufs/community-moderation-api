package snb.projects

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import snb.projects.application.dto.requests.CreateAdminRequest
import snb.projects.domain.models.commands.users.CreateUserCommand

@QuarkusIntegrationTest
class ExampleResourceIT : ExampleResourceTest() {
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
fun testCreateUser  () {
        val res = RestAssured.given()
            .accept(ContentType.JSON)
            .header("Content-Type", "application/json")
            .body(json)
            .`when`()
            .post("/users-create/meufs")
            .then().extract()
        assertEquals(204, res.statusCode())
    }
}
