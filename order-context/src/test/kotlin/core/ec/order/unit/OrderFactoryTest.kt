package core.ec.order.unit

import core.ec.common.EntityObject
import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.order.domain.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import core.ec.order.domain.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringWriter
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
                CartItem(Product(productId = "p01", productName = "product01", thePrice = 100.1,inStock = 100), 1),
                CartItem(Product(productId = "p02", productName = "product02", thePrice = 200.6,inStock = 100), 2)
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