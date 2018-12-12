package org.oso.core.repositories

import org.oso.core.entities.HelpRequester
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpRequesterRepository: CrudRepository<HelpRequester, String> {

}