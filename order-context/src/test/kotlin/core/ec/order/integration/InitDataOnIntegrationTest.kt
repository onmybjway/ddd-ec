package core.ec.order.integration

import core.ec.order.domain.model.*
import core.ec.order.data_address
import core.ec.order.data_member
import core.ec.product.domain.model.Product
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import java.util.*

@Component
@ActiveProfiles("integration")
class InitDataOnIntegrationTest : ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    lateinit var context: ApplicationContext

    override fun onApplicationEvent(event: ApplicationReadyEvent?) {
        val orderRepository = context.getBean(OrderRepository::class.java)
        (1..50).forEach { orderRepository.save(newOrder(it)) }
    }

    private fun newOrder(seed: Int): Order {
        val orderNumber = "test-order-$seed"
        val cartItems: Set<CartItem> = (0..Random().nextInt(10))
                .map {
                    CartItem(
                            Product(productId = "product$it",
                                    productName = "product-${RandomStringUtils.randomAlphabetic(6)}",
                                    thePrice = Random().nextDouble() * 100 + 0.01)
                            , Random().nextInt(999) + 1)
                }.toSet()
        return OrderFactory.createOrder(orderNumber, data_member, data_address, cartItems,
                remark = RandomStringUtils.randomAlphanumeric(200))
    }

}