package core.ec.order.integration

import core.ec.order.application.IOrderService
import core.ec.order.application.OrderCreateCommand
import core.ec.order.port.IMemberService
import core.ec.order.port.IProductService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@IntegrationTest
class OrderServiceTest {

    @Autowired
    lateinit var orderService: IOrderService

    @MockBean
    lateinit var memberService: IMemberService

    @MockBean
    lateinit var productService: IProductService

    @Test
    fun getByOrderNumber() {
        val orderNumber = "test-order-1"
        val order = orderService.getByOrderNumber(orderNumber)
        assertThat(order.isPresent).isTrue()
        assertThat(order.get().orderNumber).isEqualTo(orderNumber)

        val order2 = orderService.getByOrderNumber("not-exist-order")
        assertThat(order2.isPresent).isFalse()
    }

    @Test
    fun findByMember() {
        val orders = orderService.findByMember(1)
        assertThat(orders).hasSize(50)
    }

    @Ignore
    @Test
    fun create() {
        val newOrder = OrderCreateCommand(
                1,
                OrderCreateCommand.ShippingAddress("hebei",
                        "baoding", "lianchi", "hongxing road No.1",
                        "071000", "13000000000", "zhaoqiang"),
                setOf(
                        OrderCreateCommand.CartItem("product1", 9.99, 1),
                        OrderCreateCommand.CartItem("product2", 9.99, 2),
                        OrderCreateCommand.CartItem("product3", 9.99, 3)
                ),
                "<h1>this is remark</h1>"
        )
        val result = orderService.create(newOrder)
        assertThat(result).isNotBlank()
    }
}

