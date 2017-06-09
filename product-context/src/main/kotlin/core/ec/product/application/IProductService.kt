package core.ec.product.application


import core.ec.product.domain.model.Product
import java.util.*

interface IProductService {
    fun getByProductId(productId: String): Optional<Product>
}