package core.ec

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.modelmapper.ModelMapper

val objectMapper = ObjectMapper().registerModule(KotlinModule())

val modelMapper: ModelMapper = ModelMapper()