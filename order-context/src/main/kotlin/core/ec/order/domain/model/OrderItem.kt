package core.ec.order.domain.model

import core.ec.common.ValueObject
import core.ec.order.domain.model.Product
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.Valid
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin

/**
 * 订单项
 */
@Entity
class OrderItem private constructor() : ValueObject() {

    @Id
    @GeneratedValue
    private val id: Long = 0

    @NotBlank
    var productId: String = ""
        private set

    @DecimalMin("1")
    @DecimalMax("1000")
    var quantity: Int = 0
        private set

    @DecimalMin("0.01")
    var subtitle: BigDecimal = BigDecimal.ZERO
        private set

    @Embedded
    @Valid
    lateinit var productSnapshot: ProductSnapshot
        private set

    constructor(product: Product, qty: Int) : this() {
        this.productId = product.productId
        this.quantity = qty
        this.productSnapshot = ProductSnapshot(product)
        calculateSubTitle()
    }

    private fun calculateSubTitle() {
        val price = this.productSnapshot.price
        val qty = BigDecimal(this.quantity.toDouble())
        this.subtitle = price.multiply(qty).setScale(2, BigDecimal.ROUND_HALF_UP)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        if (!super.equals(other)) return false

        other as OrderItem

        if (productId != other.productId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + productId.hashCode()
        return result
    }


}