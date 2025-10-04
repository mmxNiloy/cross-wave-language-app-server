package com.crosswaveconsultancy.language_server.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableConfigurationProperties(SupabaseJwtProps::class)
class JwtConfig(val props: SupabaseJwtProps) {
    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = SecretKeySpec(props.secret.toByteArray(), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(key).build()
    }
}