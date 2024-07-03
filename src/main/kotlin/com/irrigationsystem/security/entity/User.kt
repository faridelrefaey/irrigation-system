package com.irrigationsystem.security.entity

import jakarta.persistence.*

@Entity
@Table(name = "_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val username: String? = null,
    val password: String? = null,
    val role: String? = null
)
