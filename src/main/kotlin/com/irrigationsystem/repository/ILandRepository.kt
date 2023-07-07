package com.irrigationsystem.repository

import com.irrigationsystem.entity.Land
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ILandRepository: JpaRepository<Land, Long> {
}