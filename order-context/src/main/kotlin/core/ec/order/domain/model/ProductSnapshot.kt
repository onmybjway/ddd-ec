package core.ec.order.domain.model


import core.ec.order.common.ValueObject
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal
import java.math.RoundingMode

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.DecimalMin

@Embeddable
class ProductSnapshot private constructor() : ValueObject() {

    @NotBlank
    @Length(max = 60)
    @Column(length = 60)
    var productName: String = ""
        private set

    @DecimalMin("0.01")
    var price: BigDecimal = BigDecimal.ZERO
        private set(v) {
            field = if (v.scale() == 2) v else v.setScale(2, RoundingMode.HALF_UP)
        }

    constructor(product: Product) : this() {
        productName = product.productName
        price = product.price
    }

}
