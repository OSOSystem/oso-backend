package org.ososystem.human.domain.entities

import java.util.*

data class Human(val id: UUID,  var name: String, var keycloakName: String? = null)