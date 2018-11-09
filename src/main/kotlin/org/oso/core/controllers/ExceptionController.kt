package org.oso.core.controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionController {
    @ExceptionHandler(Throwable::class)
    fun handleError(req: HttpServletRequest, ex: Exception) {
        LOGGER.error("Request {} raised exception", req.requestURL, ex)
        throw ex
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(ExceptionController::class.java)
    }
}