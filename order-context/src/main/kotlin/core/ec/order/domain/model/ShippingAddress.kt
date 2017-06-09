package core.ec.order.domain.model

import core.ec.order.common.ValueObject

/**

 */
class ShippingAddress(
        val province: String = "",
        val city: String = "",
        val district: String = "",
        val address: String = "",
        val zipCode: String = "",
        val contactNumber: String = "",
        val receiver: String = ""
) : ValueObject()