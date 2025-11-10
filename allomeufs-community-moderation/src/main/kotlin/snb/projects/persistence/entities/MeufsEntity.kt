package snb.projects.persistence.entities

import com.templates.persistence.entities.UsersEntity
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("MEUF")
class MeufsEntity: UsersEntity() {
}