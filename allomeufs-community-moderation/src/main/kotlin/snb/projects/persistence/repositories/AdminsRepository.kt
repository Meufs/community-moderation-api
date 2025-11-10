package com.templates.persistence.repositories

import snb.projects.persistence.entities.AdminsEntity
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

@ApplicationScoped
class AdminsRepository : PanacheRepository<AdminsEntity?> {
    fun findByIdentifier(identifier: String): Optional<AdminsEntity> {
        return find("SELECT u from AdminsEntity u WHERE (u.mail =:mail OR u.phoneNumber = :phoneNumber)", Parameters
            .with
                ("mail", identifier).and("phoneNumber", identifier))
            .firstResultOptional<AdminsEntity>()

    }
}