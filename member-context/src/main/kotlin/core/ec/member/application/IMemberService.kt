package core.ec.member.application


import core.ec.member.domain.model.Member
import java.util.*


interface IMemberService {
    fun getByMemberId(memberId:Long): Optional<Member>
}