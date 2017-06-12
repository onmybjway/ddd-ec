package core.ec.order.unit

import core.ec.common.DomainEvent
import core.ec.order.application.OrderCreateCommand
import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.order.domain.event.OrderStatusChangedEvent
import core.ec.order.domain.model.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.iterable.Extractor
import org.junit.Before
import org.junit.Test


class OrderStatusTest {
    lateinit private var order: Order

    @Before
    @Throws(Exception::class)
    fun setUp() {
        order = createNewOrder()
    }

    @Test
    fun successful_flow() {
        //compiled flow
        assertThat(order.status).isEqualTo(OrderStatus.NEW)

        order.paymentSuccess()
        assertThat(order.status).isEqualTo(OrderStatus.PAID)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(1)

        order.approved()
        assertThat(order.status).isEqualTo(OrderStatus.VALID)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(2)

        order.packageSent(Waybill.NULL)
        assertThat(order.status).isEqualTo(OrderStatus.SHIPPING)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(3)

        order.received()
        assertThat(order.status).isEqualTo(OrderStatus.COMPLETED)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(4)

        //        verify(eventBusService,times(4)).publish(isA(OrderStatusChangedEvent.class));

        assertThat(order).extracting("_events")
                .flatExtracting(allOrderStatusChangedEvent)
                .hasSize(4)


    }

    @Test
    fun payment_failed() {
        // payment failed
        assertThatThrownBy { order.invalid() }.isInstanceOf(UnsupportedOperationException::class.java)
        order.unpaid()
        assertThat(order.status).isEqualTo(OrderStatus.FAILED)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(1)

        //        verify(eventBusService,times(1)).publish(isA(OrderStatusChangedEvent.class));
        assertThat(order).extracting("_events")
                .flatExtracting(allOrderStatusChangedEvent)
                .hasSize(1)
    }

    @Test
    fun audit_not_pass() {
        order.paymentSuccess()
        order.invalid()
        assertThat(order.status).isEqualTo(OrderStatus.FAILED)
        assertThatThrownBy({ order.paymentSuccess() }).isInstanceOf(UnsupportedOperationException::class.java)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(2)

        //        verify(eventBusService,times(2)).publish(isA(OrderStatusChangedEvent.class));
        assertThat(order).extracting("_events")
                .flatExtracting(allOrderStatusChangedEvent)
                .hasSize(2)
    }

    @Test
    fun member_reject() {
        //
        order.paymentSuccess()
        order.approved()
        assertThatThrownBy({ order.approved() }).isInstanceOf(UnsupportedOperationException::class.java)
        order.packageSent(Waybill.NULL)
        order.reject()
        assertThat(order.status).isEqualTo(OrderStatus.FAILED)
        assertThat(allChangeLogsWithoutCreateLog(order)).hasSize(4)

        //        verify(eventBusService,times(4)).publish(isA(OrderStatusChangedEvent.class));
        assertThat(order).extracting("_events")
                .flatExtracting(allOrderStatusChangedEvent)
                .hasSize(4)
    }

    private fun createNewOrder(): Order {
        val member = data_member
        val address = data_address

        val cartItems = setOf(
                CartItem(Product(productId = "p01", productName = "product01", thePrice = 100.1, inStock = 100), 1),
                CartItem(Product(productId = "p02", productName = "product02", thePrice = 200.6, inStock = 100), 2)
        )

        val orderNumber = "test-01"
        return OrderFactory.createOrder(orderNumber, member, address, cartItems,
                technical = Technical("110.110.110.110", "server01"),remark =  "remark")
    }

    private val allOrderStatusChangedEvent = Extractor<Any, Collection<DomainEvent>> {
        (it as Collection<*>).filter { it is OrderStatusChangedEvent }.map { it as DomainEvent }
    }

    private val allChangeLogsWithoutCreateLog = { theOrder: Order ->
        theOrder.getLogs().toList()
                .filter { !(it.fromStatus == OrderStatus.NULL && it.toStatus == OrderStatus.NEW) }
    }
}