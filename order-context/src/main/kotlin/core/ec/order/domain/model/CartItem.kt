package core.ec.order.domain.model

import core.ec.common.ValueObject

/**

 */
class CartItem(val product: Product, val quantity: Int) : ValueObject()
