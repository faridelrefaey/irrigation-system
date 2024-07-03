package com.irrigationsystem.security.config

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class CustomPermissionEvaluator: PermissionEvaluator {
    override fun hasPermission(authentication: Authentication?, targetDomainObject: Any?, permission: Any?): Boolean {
        if(authentication == null) return false
        val loggedInUserRole = authentication.authorities.map { it.toString() }
        return loggedInUserRole.contains(targetDomainObject as String)
    }

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean {
        println("triggered")
        return false
    }
}