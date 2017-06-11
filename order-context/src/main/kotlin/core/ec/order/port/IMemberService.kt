package core.ec.order.port


import core.ec.order.domain.model.Member
import java.util.*


interface IMemberService {
    fun getByMemberId(memberId:Long): Optional<Member>
}