package core.ec.member.domain.model

import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
class Member private constructor() {
    @Id
    @GeneratedValue
    val memberId: Long = 0

    var memberName: String = ""

    var status: MemberStatus = MemberStatus.UNKNOWN

    constructor(memberName: String, status: MemberStatus = MemberStatus.ACTIVE) : this() {
        this.memberName = memberName
        this.status = status
    }
}

enum class MemberStatus {
    ACTIVE, BLOCKED, UNKNOWN

}