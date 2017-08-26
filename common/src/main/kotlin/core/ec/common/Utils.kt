package core.ec.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration
import javax.servlet.http.HttpServletRequest

/*
for JSON
 */
val objectMapper = ObjectMapper().registerModule(KotlinModule())!!

val modelMapper: ModelMapper
    get() {
        val mm = ModelMapper()
        mm.configuration.methodAccessLevel = Configuration.AccessLevel.PRIVATE
        return mm
    }

fun HttpServletRequest.getRemoteAddress(): String =
        if (this.getHeader("X-FORWARDED-FOR").isNullOrBlank()) this.remoteAddr else this.getHeader("X-FORWARDED-FOR")
