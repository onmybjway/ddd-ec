package core.ec.order.port.adapter

import core.ec.order.domain.model.Product
import core.ec.order.port.ProductServiceAdapter
import org.springframework.stereotype.Component


@Component
class ProductServiceInmemoryAdapter : ProductServiceAdapter {
    override fun findByProductIdIn(productIds: Collection<String>): Iterable<Product> {
        return listOf(
                Product("product1", "product1", 9.99, theCost = 6.66, inStock = 100),
                Product("product2", "product2", 9.99, theCost = 6.66, inStock = 100),
                Product("product3", "product3", 9.99, theCost = 6.66, inStock = 100)
        )
    }

}