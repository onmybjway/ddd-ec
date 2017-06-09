package core.ec.domain.model

import core.ec.common.ValueObject

/**

 */
class CartItem(val product: Product, val quantity: Int) : ValueObject()
