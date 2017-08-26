package core.ec.member.application

import core.ec.member.domain.model.Member
import core.ec.member.domain.model.MemberRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
        val memberRepository: MemberRepository
) {

    fun getByMemberId(memberId: Long): Optional<Member> {
        return memberRepository.getByMemberId(memberId)
    }

    fun getByMemberName(memberName: String): Optional<Member> {
        return memberRepository.getByMemberName(memberName)
    }
}