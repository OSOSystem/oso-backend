package org.oso.core.entities

import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    @CreationTimestamp
    val created: LocalDateTime = LocalDateTime.now()
    @CreatedBy
    val creator: String = ""
    @LastModifiedDate
    var updated: LocalDateTime = created
    @LastModifiedBy
    var updater: String = creator
}