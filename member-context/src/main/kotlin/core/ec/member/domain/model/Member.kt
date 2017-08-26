package core.ec.member.domain.model

import core.ec.common.EntityObject
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Member constructor(
        var memberName: String = "",
        var password: String = "",
        var status: MemberStatus = MemberStatus.UNKNOWN

) : EntityObject() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0
}

enum class MemberStatus {
    ACTIVE, BLOCKED, UNKNOWN

}