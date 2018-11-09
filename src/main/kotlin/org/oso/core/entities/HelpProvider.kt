package org.oso.core.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "hp")
data class HelpProvider(
    @Column(unique = true)
    var name: String,
    var password: String,
    var email: String? = null,
    var expoPushToken: String? = null,
    var phoneNumber: String? = null
) : BaseEntity() {

    @JsonBackReference
    @ManyToMany(mappedBy = "helpProviders")
    val helpRequesters = mutableSetOf<HelpRequester>()

    fun isAssignedTo(helpRequester: HelpRequester) =
        helpRequester.helpProviders.find { it.id == id } != null
}