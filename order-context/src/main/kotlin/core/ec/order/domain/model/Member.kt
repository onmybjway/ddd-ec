package core.ec.order.domain.model

import core.ec.common.ValueObject
import javax.persistence.Transient

class Member(
        val memberId: Long,
        val memberName: String,

        @Transient
        val status: MemberStatus = MemberStatus.UNKNOWN

) : ValueObject() {

    private constructor() : this(0, "")

}

enum class MemberStatus {
    ACTIVE, BLOCKED,UNKNOWN

}
