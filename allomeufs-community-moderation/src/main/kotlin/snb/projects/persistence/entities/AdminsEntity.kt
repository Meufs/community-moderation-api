package com.templates.persistence.entities

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("ADMIN")
class AdminsEntity:UsersEntity() {
}