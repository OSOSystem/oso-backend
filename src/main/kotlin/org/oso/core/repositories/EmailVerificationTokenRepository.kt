package org.oso.core.repositories

import org.oso.core.entities.EmailVerificationToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailVerificationTokenRepository : CrudRepository<EmailVerificationToken, String>