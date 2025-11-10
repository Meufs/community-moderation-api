package com.templates.persistence.entities

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CLIENT")
class ClientsEntity: UsersEntity() {
}