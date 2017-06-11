package core.ec.order.domain.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, Long> {
    fun getByOrderNumber(orderNumber: String): Optional<Order>
    fun findByMember_MemberIdOrderByCreateTimeDesc(memberId: Long, pageable: Pageable): Page<Order>
}