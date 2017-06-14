package core.ec.order.port



import core.ec.order.domain.model.Product
import java.util.*

interface ProductServiceAdapter {
//    fun getByProductId(productId: String): Optional<Product>
    fun findByProductIdIn(productIds:Collection<String>):Iterable<Product>
}