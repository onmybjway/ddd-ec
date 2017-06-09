package core.ec

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@org.springframework.scheduling.annotation.EnableAsync
@org.springframework.boot.autoconfigure.SpringBootApplication
class Application

fun main(args: Array<String>) {
    org.springframework.boot.SpringApplication.run(core.ec.Application::class.java, *args)
}
