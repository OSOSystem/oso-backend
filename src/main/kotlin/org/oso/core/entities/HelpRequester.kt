package org.oso.core.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name = "hr")
data class HelpRequester(
    var name: String,
    var email: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null
) : BaseEntity() {

    @JsonManagedReference
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "hr_hp", joinColumns = [JoinColumn(name = "hr_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "hp_id", referencedColumnName = "id")])
    val helpProviders = mutableSetOf<HelpProvider>()

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "helpRequester")
    val devices = mutableSetOf<Device>()
}