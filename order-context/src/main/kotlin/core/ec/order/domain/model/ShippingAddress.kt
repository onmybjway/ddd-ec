package core.ec.order.domain.model

import core.ec.common.ValueObject

/**

 */
class ShippingAddress(
        province: String = "",
        city: String = "",
        district: String = "",
        address: String = "",
        zipCode: String = "",
        contactNumber: String = "",
        receiver: String = ""
) : ValueObject() {
    var province = province
        private set
    var city = city
        private set
    var district = district
        private set
    var address = address
        private set
    var zipCode = zipCode
        private set
    var contactNumber = contactNumber
        private set
    var receiver = receiver
        private set
}