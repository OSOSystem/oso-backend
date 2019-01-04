package org.oso.core.repositories

import org.oso.core.entities.Emergency
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmergencyRepository : CrudRepository<Emergency, String> {

}