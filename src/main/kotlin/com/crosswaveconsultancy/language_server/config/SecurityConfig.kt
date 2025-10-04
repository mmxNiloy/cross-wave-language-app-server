package com.crosswaveconsultancy.language_server.config

import com.crosswaveconsultancy.language_server.exceptions.UnauthorizedException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests({ auth ->
                auth
                    .anyRequest().permitAll()
            }
            )
            .oauth2ResourceServer({ oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter { token ->
                        val authorities = (token.claims["role"] as? String)
                            ?.let { listOf(SimpleGrantedAuthority("ROLE_${it.uppercase()}")) }
                            ?: emptyList()
                        JwtAuthenticationToken(token, authorities)
                    }
                }.authenticationEntryPoint { request, response, authException ->
                    authException.printStackTrace()
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
                }
            })

        return httpSecurity.build()
    }
}