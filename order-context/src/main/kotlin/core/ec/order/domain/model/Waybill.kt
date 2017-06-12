package core.ec.order.domain.model

import core.ec.common.ValueObject
import java.util.*
import javax.persistence.Embeddable

@Embeddable
class Waybill(
        val number: String,
        val company: String,
        val sentAt: Date?
) : ValueObject() {

    companion object {
        val NULL=Waybill("","",null)
    }
}