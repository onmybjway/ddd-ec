package core.ec.order.domain.model

import core.ec.common.ValueObject
import javax.persistence.Transient

class Member constructor(
        memberId: Long = 0,
        memberName: String = "",
        status: MemberStatus = MemberStatus.UNKNOWN

) : ValueObject() {

    var memberId = memberId
        private set

    var memberName: String = memberName
        private set

    @Transient
    var status: MemberStatus = status
        private set
}

enum class MemberStatus {
    ACTIVE, BLOCKED, UNKNOWN

}
