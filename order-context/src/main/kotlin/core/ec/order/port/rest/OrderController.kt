package core.ec.order.port.rest

import core.ec.order.application.IOrderService
import core.ec.order.application.OrderCreateCommand
import core.ec.order.getRemoteAddress
import core.ec.order.modelMapper
import core.ec.order.port.dto.Order
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
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
    fun create(@RequestBody @Valid createCmd: OrderCreateCommand, request: HttpServletRequest): String {
        createCmd.technical = OrderCreateCommand.Technical(request.getRemoteAddress(),""/*TODO: implement form server*/)
        val orderNumber = orderService.create(createCmd)
        return """{"result":"success","order-number":"$orderNumber"}"""
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "{orderNumber}")
    fun getById(@PathVariable orderNumber: String): Order {
        val order = orderService.getByOrderNumber(orderNumber)
        return modelMapper.map(order, Order::class.java)
    }

}