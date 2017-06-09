package core.ec.order.unit

import core.ec.common.EntityObject
import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.domain.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringWriter
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(EventPublishService.class)
class OrderFactoryTest {

    @Test
    fun createOrder() {
        val member = data_member
        val address = data_address

        val cartItems = setOf(
                CartItem(Product("p01", "product01", 100.1), 1),
                CartItem(Product("p02", "product02", 200.6), 2)
        )

        val orderNumber = "test-01"
        val order = OrderFactory.createOrder(orderNumber, member, address, cartItems, "this is remark")

        assertThat(order).isNotNull()
        assertThat(order.orderNumber).isEqualTo(orderNumber)
        assertThat(order.shippingAddress).isEqualTo(address)
        assertThat(order.getItems()).isNotEmpty.hasSize(2)
        assertThat(order.getAmount()).isEqualTo(501.3)
        assertThat(order.remark).isEqualTo("this is remark")
        assertThat(order.member.memberId).isEqualTo(member.memberId)
        assertThat(order.status).isEqualTo(OrderStatus.NEW)

        //        verify(eventBusService, times(1)).publish(isA(OrderCreatedEvent.class));

        val filed = EntityObject::class.memberProperties.first({ kProperty1 -> kProperty1.name == "_events" })
        filed.isAccessible = true
        val events = filed.get(order) as Collection<*>
        assertThat(events).isNotNull
        assertThat(events).hasSize(1)

        val mapper = ObjectMapper().registerModule(KotlinModule())
        val writer:StringWriter= StringWriter()
        mapper.writeValue(writer, order)
        println(writer)
    }
}