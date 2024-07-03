package com.irrigationsystem.repository

import com.irrigationsystem.entity.Land
import com.irrigationsystem.security.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ILandRepository: JpaRepository<Land, Long> {

    fun findByUserId(userId: Long): List<Land>

    @Query("SELECT l from Land l where l.id = :id and l.user = :user")
    fun findLandById(id: Long, user: User): Land?
}