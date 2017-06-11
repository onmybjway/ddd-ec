package core.ec.order.domain.model

import core.ec.common.ValueObject
import java.math.BigDecimal
import java.math.RoundingMode


class Product(
        val productId: String,
        val productName: String,
        thePrice: Double = 0.00,
        theCost: Double = 0.00,
        val inStock: Int = 0

) : ValueObject() {


    val price: BigDecimal = BigDecimal.valueOf(thePrice).setScale(2, RoundingMode.HALF_UP)
    val cost: BigDecimal = BigDecimal.valueOf(theCost).setScale(2, RoundingMode.HALF_UP)

    private constructor() : this("", "")

}
