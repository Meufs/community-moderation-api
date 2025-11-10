package snb.projects.persistence.repositories

import com.templates.persistence.entities.UsersEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

@ApplicationScoped
class UsersRepository : PanacheRepository<UsersEntity?> {

    fun findByIdentifier(identifier: String): Optional<UsersEntity> {
        return find("SELECT u from UsersEntity u WHERE (u.mail =:mail OR u.phoneNumber = :phoneNumber)", Parameters.with("mail", identifier).and("phoneNumber", identifier))
            .firstResultOptional<UsersEntity>()

    }
}