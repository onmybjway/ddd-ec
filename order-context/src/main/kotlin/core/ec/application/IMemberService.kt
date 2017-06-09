package core.ec.application

import core.ec.domain.model.Member
import java.util.*


interface IMemberService {
    fun getByMemberId(memberId:Long): Optional<Member>
}