package core.ec.order.domain.model

import core.ec.common.ValueObject
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Technical(
        remoteAddress: String,
        viaNode: String
)
    : ValueObject() {

    @Column(length = 15)
    var remoteAddress = remoteAddress
        private set

    @Column(length = 15)
    var viaNode = viaNode
        private set

    private constructor() : this(remoteAddress = "", viaNode = "")
}