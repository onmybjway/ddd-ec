package core.ec.order.domain.model

import core.ec.common.ValueObject
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Waybill(

        @Column(name = "waybill_number")
        val number: String,

        val company: String,

        val sentAt: Date?
) : ValueObject() {

    private constructor() : this("", "", null)

    companion object {
        val NULL = Waybill("", "", null)
    }
}