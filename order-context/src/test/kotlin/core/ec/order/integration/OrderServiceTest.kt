package core.ec.order.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import core.ec.order.application.IOrderService
import core.ec.order.application.OrderCreateCommand
import core.ec.order.objectMapper
import core.ec.order.port.MemberServiceAdapter
import core.ec.order.port.ProductServiceAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import java.io.StringWriter


@RunWith(SpringRunner::class)
@SpringBootTest
@IntegrationTest
class OrderServiceTest {

    @Autowired
    lateinit var orderService: IOrderService

//    @MockBean
//    lateinit var memberServiceAdapter: MemberServiceAdapter

//    @MockBean
//    lateinit var productServiceAdapter: ProductServiceAdapter

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


        val writer: StringWriter = StringWriter()
        objectMapper.writeValue(writer, newOrder)
        println(writer)
    }
}

