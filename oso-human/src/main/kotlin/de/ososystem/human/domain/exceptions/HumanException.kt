package de.ososystem.human.domain.exceptions

open class HumanException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)