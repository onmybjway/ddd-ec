package core.ec.order.domain.model

class OrderFactory private constructor(){

    companion object {
        fun createOrder(
                orderNumber: String,
                member: Member,
                address: ShippingAddress,
                cartItems: Set<CartItem>,
                remark: String = ""
        ): Order {
            if (cartItems.isEmpty()) throw IllegalArgumentException("cartItems is Empty")

            val order = Order(orderNumber, member, address,remark)
            cartItems.forEach { order.addItem(it.product, it.quantity) }

            return order
        }
    }

}
