package core.ec.order.application

import core.ec.order.domain.model.Member
import java.util.*


interface IMemberService {
    fun getByMemberId(memberId:Long): Optional<Member>
}