package org.oso.core.entities

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Coordinates (
    @Column(columnDefinition = "DECIMAL(9, 7)")
    val latitude: BigDecimal,
    @Column(columnDefinition = "DECIMAL(10, 7)")
    val longitude: BigDecimal
)