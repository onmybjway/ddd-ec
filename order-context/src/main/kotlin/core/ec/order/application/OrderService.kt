package core.ec.order.application

import core.ec.order.port.IMemberService
import core.ec.order.exceptions.*
import core.ec.order.domain.event.OrderCreatedEvent
import core.ec.order.domain.event.OrderStatusChangedEvent
import core.ec.order.domain.model.*
import core.ec.order.modelMapper
import core.ec.order.port.IProductService
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
        if(member.status !=MemberStatus.ACTIVE) throw MemberUnavailableException(createCmd.memberId)

        val address = modelMapper.map(createCmd.address, ShippingAddress::class.java)

        val items = createCmd.cartItems.map {
            val product = productService.getByProductId(it.productId)
                    .orElseThrow { -> ProductNotFoundException(it.productId) }

            //todo:this logic can extract to a function
            if (product.price != BigDecimal.valueOf(it.price)) throw ProductNotMatchException(it.productId)

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

