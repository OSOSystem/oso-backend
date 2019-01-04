package org.oso.core.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "hr")
data class HelpRequester(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Size(min = 1)
    var id: String? = null,
    @Size(min = 1)
    var name: String,
    @Size(min = 1)
    var keycloakName: String
) {
    @JsonManagedReference
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "hr_hp", joinColumns = [JoinColumn(name = "hr_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "hp_id", referencedColumnName = "id")])
    val helpProviders = mutableSetOf<HelpProvider>()

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "helpRequester")
    val devices = mutableSetOf<Device>()
}