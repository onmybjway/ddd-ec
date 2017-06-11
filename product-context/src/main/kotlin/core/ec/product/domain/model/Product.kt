package core.ec.product.domain.model

import core.ec.common.ValueObject

import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.Entity


class Product(
        val productId: String,
        val productName: String,
        thePrice: Double
) : ValueObject() {

    val stock:Stock = Stock.ZERO

    val price: BigDecimal = BigDecimal.valueOf(thePrice).setScale(2, RoundingMode.HALF_UP)

    private constructor() : this("", "", 0.00)

}
