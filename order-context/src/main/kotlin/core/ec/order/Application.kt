package core.ec.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication(scanBasePackages = arrayOf("core.ec"))
class Application

fun main(args: Array<String>) {
    org.springframework.boot.SpringApplication.run(Application::class.java, *args)
}
