package core.ec.order.integration

import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.order.domain.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

/**
 */
@RunWith(SpringRunner::class)
@DataJpaTest
class OrderJpaTests {
    @Autowired
    lateinit private var entityManager: TestEntityManager

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun create_order() {

        val orderNumber = "test-order"

        val member = data_member
        val address = data_address

        val product01 = Product("product01", "product01", 100.01)
        val product02 = Product("product02", "product02", 200.053)
        val cartItems = setOf(CartItem(product01, 2), CartItem(product02, 1))

        val order = OrderFactory.createOrder(orderNumber, member, address, cartItems, "")
        order.paymentSuccess()
        order.approved()

        orderRepository.save(order)

        val orderFromDB = orderRepository.getByOrderNumber(orderNumber).get()
        assertThat(orderFromDB.getAmount()).isEqualTo(400.07)
        assertThat(orderFromDB.status).isEqualTo(OrderStatus.VALID)

        val newOrderLogs = orderFromDB.getLogs().toList()
        assertThat(newOrderLogs).hasSize(3);

        assertThat(newOrderLogs).filteredOn {
            it.fromStatus == OrderStatus.NULL && it.toStatus == OrderStatus.NEW
        }.hasSize(1)
    }
}