package core.ec.order.application

import core.ec.order.domain.event.OrderCreatedEvent
import core.ec.order.domain.event.OrderStatusChangedEvent
import core.ec.order.domain.model.*
import core.ec.order.exceptions.MemberNotFoundException
import core.ec.order.exceptions.MemberUnavailableException
import core.ec.order.exceptions.ProductNotFoundException
import core.ec.order.exceptions.ProductNotMatchException
import core.ec.order.modelMapper
import core.ec.order.port.MemberServiceAdapter
import core.ec.order.port.ProductServiceAdapter
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
        val memberService: MemberServiceAdapter,
        val productService: ProductServiceAdapter

) : IOrderService {

    override fun getByOrderNumber(orderNumber: String): Optional<Order> {
        return this.orderRepository.getByOrderNumber(orderNumber)
    }

    override fun findByMember(memberId: Long, pageable: Pageable): Page<Order>
            = this.orderRepository.findByMember_MemberIdOrderByCreateTimeDesc(memberId, pageable)

    @Transactional
    override fun create(createCmd: OrderCreateCommand): String {
        //get the member
        val member = memberService.getByMemberId(createCmd.memberId)
                .orElseThrow { -> MemberNotFoundException(createCmd.memberId) }

        if (member.status != MemberStatus.ACTIVE) throw MemberUnavailableException(createCmd.memberId)

        val address = modelMapper.map(createCmd.address, ShippingAddress::class.java)

        val technical = modelMapper.map(createCmd.technical, Technical::class.java)

/*        val items = createCmd.cartItems.map {
            //todo:需要优化为批量查询
            val product = productService.getByProductId(it.productId)
                    .orElseThrow { -> ProductNotFoundException(it.productId) }

            //todo:思考这部分验证商品价格变更的逻辑是否应该放在领域模型里
            //todo:this logic can extract to a function
            if (product.price != BigDecimal.valueOf(it.price)) throw ProductNotMatchException(it.productId)

            CartItem(product, it.quantity)
        }.toSet()*/

        val allProduct = productService.findByProductIdIn(createCmd.cartItems.map { it.productId })
        val items = createCmd.cartItems.map { item ->
            val product = Optional.ofNullable(allProduct.filter { it.productId == item.productId }.firstOrNull())
                    .orElseThrow { -> ProductNotFoundException(item.productId) }

            //todo:思考这部分验证商品价格变更的逻辑是否应该放在领域模型里
            //todo:this logic can extract to a function
            if (product.price != BigDecimal.valueOf(item.price)) throw ProductNotMatchException(item.productId)

            CartItem(product, item.quantity)
        }.toSet()

        val order = OrderFactory.createOrder(
                orderNumber = generateOrderNumber(), member = member, address = address,
                cartItems = items, technical = technical, remark = createCmd.remark
        )

        return this.orderRepository.save(order).orderNumber
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

