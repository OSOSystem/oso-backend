package org.oso.core.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class HelpRequesterNotFoundException(message: String) : RuntimeException(message)