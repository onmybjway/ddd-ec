package core.ec.order.application

import core.ec.order.domain.model.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.transaction.Transactional

interface IOrderService {
    fun getByOrderNumber(orderNumber: String): Optional<Order>

    fun findByMember(memberId: Long, pageable: Pageable): Page<Order>

    @Transactional
    fun create(createCmd: OrderCreateCommand): String
}