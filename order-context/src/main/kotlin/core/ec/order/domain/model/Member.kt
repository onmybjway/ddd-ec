package core.ec.order.domain.model

import core.ec.common.ValueObject

/**

 */
class Member(
        val memberId: Long,
        val memberName: String
) : ValueObject() {
    private constructor() : this(0, "")

}
