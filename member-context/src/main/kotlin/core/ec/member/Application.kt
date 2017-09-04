package core.ec.member

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("core.ec"))
class Application

fun main(args: Array<String>) {
    org.springframework.boot.SpringApplication.run(core.ec.member.Application::class.java, *args)
}
