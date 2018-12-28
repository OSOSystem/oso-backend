package org.oso.core.services.impl

import org.oso.core.services.SecurityService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Component
class DefaultSecurityService() : SecurityService {
    override fun getCurrentUserName() =
        SecurityContextHolder.getContext().authentication.name
}