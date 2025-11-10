package snb.projects.persistence.repositories

import snb.projects.persistence.entities.MeufsEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

@ApplicationScoped
class ClientsRepository : PanacheRepository<MeufsEntity?> {
    fun findByIdentifier(identifier: String): Optional<MeufsEntity> {
        return find("SELECT u from ClientsEntity u WHERE (u.mail =:mail OR u.phoneNumber = :phoneNumber)", Parameters
            .with
            ("mail", identifier).and("phoneNumber", identifier))
            .firstResultOptional<MeufsEntity>()

    }
}