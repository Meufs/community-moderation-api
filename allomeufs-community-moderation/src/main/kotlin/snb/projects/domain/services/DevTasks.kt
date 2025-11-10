package snb.projects.domain.services

import io.quarkus.arc.profile.IfBuildProfile
import io.quarkus.runtime.Shutdown
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@IfBuildProfile("dev")
class DevTasks {
    @Shutdown
    fun shutdown() {
        //TODO some task when app shutdowns in dev mode
        println("Shutting down")
    }
}