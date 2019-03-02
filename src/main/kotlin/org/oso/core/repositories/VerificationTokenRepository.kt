package org.oso.core.repositories

import org.oso.core.entities.VerificationToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository : CrudRepository<VerificationToken, String>