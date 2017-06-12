package core.ec.order.unit

import core.ec.order.domain.model.Order
import core.ec.order.domain.model.OrderStatus
import core.ec.order.domain.model.ProductSnapshot
import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.order.domain.model.Product
import core.ec.order.exceptions.ProductOutOfStockException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.util.*


@RunWith(SpringRunner::class)
class OrderTests {
    private val member = data_member
    private val address = data_address

    @Test
    fun initOrder_with_basc_logic() {

        val order1 = Order("test-order", member, address)
        val order2 = Order("test-order", member, address)

        assertThat(order1).isEqualTo(order2)

        val orders = HashSet<Order>()
        orders.add(order1)
        orders.add(order2)
        assertThat(orders).hasSize(1)
    }

    @Test
    fun initOrder_should_success() {
        val date = Date()
        Thread.sleep(1)

        assertThatThrownBy { Order("", member, address) }.isInstanceOf(IllegalArgumentException::class.java)

        val order = Order("123456", member, address)
        assertThat(order).isNotNull()
        assertThat(order.status).isEqualTo(OrderStatus.NEW)
        assertThat(order.createTime).isAfterOrEqualsTo(date)
        assertThat(order.createTime).isEqualTo(order.lastChangeTime)
        assertThat(order.getItems().toList()).isEmpty()

        // properties ...
        order.remark = "hello"
        assertThat(order.remark).isEqualTo("hello")
    }

    @Test
    fun addItem_should_success() {
        val order = Order("test-order", member, address)
        val product01 = Product(productId = "product01", productName = "product01", thePrice = 100.017,inStock = 100)
        val product02 = Product(productId = "product02", productName = "product02", thePrice = 200.053,inStock = 100)

        // add product into order
        order.addItem(product01, 2)
        val items = order.getItems().toList()
        assertThat(items).isNotEmpty.hasSize(1)
        assertThat(items).extracting("productId").contains(product01.productId)
        assertThat(order.getAmount()).isEqualTo(200.04)

        // thrown exception when add duplicate product into order items
        assertThatThrownBy { order.addItem(product01, 2) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("do not add duplicate product")
        assertThat(order.getItems().toList()).isNotEmpty.hasSize(1)
        assertThat(order.getAmount()).isEqualTo(200.04)

        // test second product to item
        order.addItem(product02, 1)
        assertThat(order.getItems().toList()).isNotEmpty.hasSize(2)
        assertThat(order.getItems().toList()).extracting("productId")
                .contains(product01.productId, product02.productId)

        assertThat(order.getAmount()).isEqualTo(400.09)
    }

    @Test
    fun addItem_should_fail_when_product_out_stock() {
        val order = Order("test-order", member, address)
        val product01 = Product(productId = "product01", productName = "product01", thePrice = 100.017,inStock = 100)

        assertThatThrownBy { order.addItem(product01, 200) }.isInstanceOf(ProductOutOfStockException::class.java)
    }


}
