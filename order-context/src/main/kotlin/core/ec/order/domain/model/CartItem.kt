package core.ec.order.domain.model

import core.ec.common.ValueObject
import core.ec.product.domain.model.Product

/**

 */
class CartItem(val product: Product, val quantity: Int) : ValueObject()
