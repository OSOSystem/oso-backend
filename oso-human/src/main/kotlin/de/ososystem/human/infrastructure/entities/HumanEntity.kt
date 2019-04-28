package de.ososystem.human.infrastructure.entities

import de.ososystem.human.domain.entities.Human
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "human")
class HumanEntity(
    @Id
    val id: UUID,
    val name: String,
    val keycloakName: String?
) {
    fun toHuman() = Human(id, name, keycloakName)
}

fun Human.toEntity() = HumanEntity(id, name, keycloakName)