package core.ec.application

import core.ec.domain.model.Order
import java.util.*
import javax.transaction.Transactional

interface IOrderService {
    fun getByOrderNumber(orderNumber: String): Optional<Order>

    fun findByMember(memberId: Long): Iterable<Order>

    @Transactional
    fun create(createCmd: OrderCreateCommand): String
}