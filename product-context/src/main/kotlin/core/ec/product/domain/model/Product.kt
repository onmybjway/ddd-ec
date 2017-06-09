package core.ec.product.domain.model

import core.ec.common.ValueObject

import java.math.BigDecimal
import java.math.RoundingMode

class Product(
        val productId: String,
        val productName: String,
        thePrice: Double
) : ValueObject() {

    val price: BigDecimal = BigDecimal.valueOf(thePrice).setScale(2, RoundingMode.HALF_UP)

    private constructor() : this("", "", 0.00)

}
