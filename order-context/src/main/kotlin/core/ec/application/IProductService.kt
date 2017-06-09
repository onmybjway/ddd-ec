package core.ec.application

import core.ec.domain.model.Product
import java.util.*

interface IProductService {
    fun getByProductId(productId: String): Optional<Product>
}