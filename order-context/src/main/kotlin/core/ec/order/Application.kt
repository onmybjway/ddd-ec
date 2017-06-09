package core.ec.order

@org.springframework.scheduling.annotation.EnableAsync
@org.springframework.boot.autoconfigure.SpringBootApplication
class Application

fun main(args: Array<String>) {
    org.springframework.boot.SpringApplication.run(core.ec.order.Application::class.java, *args)
}
