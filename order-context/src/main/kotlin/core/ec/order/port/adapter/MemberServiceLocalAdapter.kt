package core.ec.order.port.adapter

import core.ec.member.application.MemberService
import core.ec.order.domain.model.Member
import core.ec.order.domain.model.MemberStatus
import core.ec.order.exceptions.MemberNotFoundException
import core.ec.order.modelMapper
import core.ec.order.port.MemberServiceAdapter
import org.springframework.stereotype.Component
import java.util.*

@Component
class MemberServiceLocalAdapter(
        val memberService: MemberService
) : MemberServiceAdapter {
    override fun getByMemberId(memberId: Long): Optional<Member> {
        val member = memberService.getByMemberId(memberId).orElseThrow { MemberNotFoundException(memberId) }
        return Optional.of(modelMapper.map(member,Member::class.java))
//        return Optional.of(Member(member.memberId, member.memberName, MemberStatus.valueOf(member.valid.toString())))
    }
}