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


/*    constructor(
            memberName: String,
            status: MemberStatus = MemberStatus.ACTIVE,
            password: String


    ) : this() {
        this.memberName = memberName
        this.status = status
        this.password = password

    }*/

}

enum class MemberStatus {
    ACTIVE, BLOCKED, UNKNOWN

}