package com.irrigationsystem.entity

import com.irrigationsystem.security.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Land(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var seedType: String? = null,
    var landName: String? = null,
    var area: Double? = null,
    @OneToMany(mappedBy = "land", cascade = [CascadeType.ALL])
    var irrigationConfigurationList: MutableList<IrrigationConfiguration> = mutableListOf(),
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
)
