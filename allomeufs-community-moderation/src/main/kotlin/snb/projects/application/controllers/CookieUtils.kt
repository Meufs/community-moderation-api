package snb.projects.application.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.NewCookie

@ApplicationScoped
class CookieUtils {
    fun setUpCookie(name:String, value:String) : NewCookie {
        return NewCookie.Builder(name).value(value).maxAge(64800).httpOnly(name == "Bearer").path("/").build()
    }
}