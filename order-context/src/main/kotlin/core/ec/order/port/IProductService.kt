package core.ec.order.port



import core.ec.order.domain.model.Product
import java.util.*

interface IProductService {
    fun getByProductId(productId: String): Optional<Product>
}