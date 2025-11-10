package snb.projects

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.Cookie
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import snb.projects.domain.models.users.UserTypes
import snb.projects.domain.services.JwtTokenGenerator

@QuarkusTest
class HealthCheckResourceTest {

    var cookie : Cookie? = null

    @Inject
    private lateinit var jwtTokenGenerator: JwtTokenGenerator

    @BeforeEach
    fun before() {
        val token = jwtTokenGenerator.getToken("sae.benn@gmail.com", UserTypes.MEUF.name)
        cookie = Cookie.Builder("Bearer", token)
            .setSecured(true) // Sets cookie to secure (optional)
            .setPath("/") // Defines the path (optional)
            .setMaxAge(64800) // Sets the cookie expiration to 3600 seconds (1 hour)
            .build()
    }

    @Test
    fun testHelloEndpoint() {
        given()
            .`when`().get("/healthcheck")
            .then()
            .statusCode(200)
            .body(`is`("Application is up and running"))
    }

    @Test
    fun testJwtOkEndpoint() {
        given()
            .`when`()
            .cookie(cookie)
            .get("/healthcheck/secured")
            .then()
            .statusCode(200)
            .body(equalTo("Application is up and running hahaha"))

    }

    @Test
    fun testSecuredWithNoJwtEndpoint() {
        given()
            .`when`()
            .get("/healthcheck/secured")
            .then()
            .statusCode(401)
    }

}