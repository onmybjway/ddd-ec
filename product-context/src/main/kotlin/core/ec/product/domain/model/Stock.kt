package core.ec.product.domain.model

import core.ec.common.EntityObject
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
class Stock private constructor() : EntityObject() {

    @Id
    var productId: String = ""

    @Version
    val version: Int = 0

    val forSale: Int
        get() = inStock + future - locked
    var locked: Int = 0
        private set
    var inStock: Int = 0
        private set
    var future: Int = 0
        private set

    companion object {
        val ZERO = Stock()
    }

}