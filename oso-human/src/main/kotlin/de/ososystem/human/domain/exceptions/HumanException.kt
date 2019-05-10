package de.ososystem.human.domain.exceptions

import java.lang.RuntimeException

open class HumanException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)