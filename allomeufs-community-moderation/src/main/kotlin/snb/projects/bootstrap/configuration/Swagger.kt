package snb.projects.bootstrap.configuration

import jakarta.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme

@OpenAPIDefinition(
    info = Info(
        title = "Meufs modz",
        version = "0.1.0",
        description = "Modération intra users de allo meufs API",
        license = License(name = "Apache 2.0", url = "https://foo.bar"),
        contact = Contact(name = "Sid", email = "sa.bennaceur@gmail.com")
    )
)
@SecurityScheme(
    apiKeyName ="Bearer",
    scheme = "bearer",
    securitySchemeName = "bearer",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.COOKIE,

)
@SecurityScheme(
    scheme = "api-key",
    securitySchemeName = "api_key",
    type = SecuritySchemeType.APIKEY,
    apiKeyName = "API-KEY",
    `in` = SecuritySchemeIn.HEADER
)
class Swagger : Application()
