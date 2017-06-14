package core.ec.member.domain.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


interface MemberRepository : JpaRepository<Member, Long> {
    fun getByMemberId(memberId: Long): Optional<Member>
}