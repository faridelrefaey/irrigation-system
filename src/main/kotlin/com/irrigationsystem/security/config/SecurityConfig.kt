package com.irrigationsystem.security.config

import com.irrigationsystem.security.filter.JwtTokenValidator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun jwtValidator(): JwtTokenValidator {
        return JwtTokenValidator()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun customPermissionEvaluator(): CustomPermissionEvaluator {
        return CustomPermissionEvaluator()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf{csrfCustomizer -> csrfCustomizer.disable()}
            .sessionManagement{session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests{requests ->
                requests.requestMatchers("/auth/register", "/auth/login").permitAll()
                .anyRequest().authenticated()
            }
            .formLogin{ form -> form.disable() }
            .addFilterBefore(jwtValidator(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}