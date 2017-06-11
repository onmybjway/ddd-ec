package core.ec.order.domain.model

class Product(
        val productId: String,
        val productName: String,
        thePrice: Double
) : core.ec.common.ValueObject() {

    val price: java.math.BigDecimal = java.math.BigDecimal.valueOf(thePrice).setScale(2, java.math.RoundingMode.HALF_UP)

    private constructor() : this("", "", 0.00)

}
