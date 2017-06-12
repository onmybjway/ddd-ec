package core.ec.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.modelmapper.ModelMapper
import javax.servlet.http.HttpServletRequest

/*
for JSON
 */
val objectMapper = ObjectMapper().registerModule(KotlinModule())

val modelMapper: ModelMapper = ModelMapper()

fun HttpServletRequest.getNetAddress(): String =
        if (this.getHeader("X-FORWARDED-FOR").isNullOrBlank()) this.remoteAddr else this.getHeader("X-FORWARDED-FOR")
