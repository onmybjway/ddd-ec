package core.ec.port.rest

import core.ec.application.IOrderService
import core.ec.application.OrderCreateCommand
import core.ec.modelMapper
import core.ec.port.dto.Order
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/order")
class OrderController(
        val orderService: IOrderService
) {
/*    @Autowired
    lateinit var orderRepository: OrderRepository*/

    @RequestMapping("hi")
    fun index(): String {
        return "Hi"
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun create(@RequestBody @Valid orderCreateCommand: OrderCreateCommand): String {
        val orderNumber = orderService.create(orderCreateCommand)
        return """{"result":"success","order-number":"$orderNumber"}"""
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "{orderNumber}")
    fun getById(@PathVariable orderNumber: String): Order {
        val order = orderService.getByOrderNumber(orderNumber)
        return modelMapper.map(order, Order::class.java)
    }

}