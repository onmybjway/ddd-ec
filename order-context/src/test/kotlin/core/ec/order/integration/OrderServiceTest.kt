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
import org.springframework.data.domain.PageRequest
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
        val orders = orderService.findByMember(1, PageRequest(0, 100))
        assertThat(orders).hasSize(50)
    }

    @Ignore
    @Test
    fun create() {
        val newOrder = OrderCreateCommand(
                memberId = 1,
                address = OrderCreateCommand.ShippingAddress("hebei",
                        "baoding", "lianchi", "hongxing road No.1",
                        "071000", "13000000000", "zhaoqiang"),
                cartItems = setOf(
                        OrderCreateCommand.CartItem("product1", 9.99, 1),
                        OrderCreateCommand.CartItem("product2", 9.99, 2),
                        OrderCreateCommand.CartItem("product3", 9.99, 3)
                ),
                remark = "<h1>this is remark</h1>",
                technical = OrderCreateCommand.Technical("110.110.110.110", "server01")
        )
        val result = orderService.create(newOrder)
        assertThat(result).isNotBlank()
    }
}

