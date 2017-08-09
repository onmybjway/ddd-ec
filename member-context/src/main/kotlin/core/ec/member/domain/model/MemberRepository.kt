package core.ec.member.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface MemberRepository : JpaRepository<Member, Long> {
    fun getByMemberId(memberId: Long): Optional<Member>
    fun getByMemberName(memberName: String): Optional<Member>
}