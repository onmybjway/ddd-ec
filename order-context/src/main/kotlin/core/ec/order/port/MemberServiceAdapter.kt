package core.ec.order.port


import core.ec.order.domain.model.Member
import java.util.*


interface MemberServiceAdapter {
    fun getByMemberId(memberId:Long): Optional<Member>
}