package org.oso.core.repositories

import org.oso.core.entities.HelpProvider
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HelpProviderRepository: CrudRepository<HelpProvider, String> {

}