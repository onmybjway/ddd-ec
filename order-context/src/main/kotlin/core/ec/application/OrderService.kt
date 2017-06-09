package core.ec.application

import core.ec.application.exceptions.*
import core.ec.domain.event.OrderCreatedEvent
import core.ec.domain.event.OrderStatusChangedEvent
import core.ec.domain.model.*
import core.ec.modelMapper
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import java.math.BigDecimal
import java.util.*
import javax.transaction.Transactional

/**
 *
 */
@Service
class OrderService(
        val orderRepository: OrderRepository,
        val memberService: IMemberService,
        val productService: IProductService

) : IOrderService {

    override fun getByOrderNumber(orderNumber: String): Optional<Order> {
        return this.orderRepository.getByOrderNumber(orderNumber)
    }

    override fun findByMember(memberId: Long): Iterable<Order> = this.orderRepository.findByMember_MemberId(memberId)

    @Transactional
    override fun create(createCmd: OrderCreateCommand): String {
        //get the member
        val member = memberService.getByMemberId(createCmd.memberId)
                .orElseThrow { -> MemberNotFoundException(createCmd.memberId) }

        val address = modelMapper.map(createCmd.address, ShippingAddress::class.java)

        val items = createCmd.cartItems.map {
            val product = productService.getByProductId(it.productId)
                    .orElseThrow { -> ProductNotFoundException(it.productId) }

            //todo:can extract to fun
            if (product.price != BigDecimal.valueOf(it.price)) throw ProductNotMatchException(it.productId)

            //todo:check stock

            CartItem(product, it.quantity)
        }.toSet()

        val order = OrderFactory.createOrder(generateOrderNumber(), member, address, items, createCmd.remark)

        return this.orderRepository.save(order).orderNumber
//        return order.orderNumber
    }

    @Async
    @EventListener
    fun OnCreated(event: OrderCreatedEvent) {
        logger.debug("event received:{}", event.orderNumber)
    }

    @Async
    //    @EventListener
    @TransactionalEventListener
    fun OnChanged(event: OrderStatusChangedEvent) {
        logger.debug("order changed:{}", event)
    }

    private fun generateOrderNumber(): String = RandomStringUtils.randomAlphanumeric(6)

    companion object {
        private val logger = LoggerFactory.getLogger(OrderService::class.java)
    }
}
