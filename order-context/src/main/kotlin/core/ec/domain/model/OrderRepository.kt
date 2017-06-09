package core.ec.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface OrderRepository : JpaRepository<Order, Long> {
    fun getByOrderNumber(orderNumber: String): Optional<Order>
    fun findByMember_MemberId(memberId: Long): Collection<Order>


}
