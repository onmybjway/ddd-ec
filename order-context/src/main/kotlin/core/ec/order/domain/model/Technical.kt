package core.ec.order.domain.model

import core.ec.common.ValueObject
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Technical(
        @Column(length = 15)
        val remoteAddress: String,

        @Column(length = 15)
        val viaNode: String
)
    : ValueObject() {

    private constructor() : this(remoteAddress = "", viaNode = "")
}