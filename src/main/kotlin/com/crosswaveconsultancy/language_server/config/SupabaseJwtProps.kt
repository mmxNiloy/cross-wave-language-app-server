package com.crosswaveconsultancy.language_server.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "supabase.jwt")
data class SupabaseJwtProps(
    var secret: String = ""
)