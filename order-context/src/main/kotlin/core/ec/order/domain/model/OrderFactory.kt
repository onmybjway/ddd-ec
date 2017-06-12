package core.ec.order.domain.model

class OrderFactory private constructor(){

    companion object {
        fun createOrder(
                orderNumber: String,
                member: Member,
                address: ShippingAddress,
                cartItems: Set<CartItem>,
                technical: Technical,
                remark: String = ""
        ): Order {
            if (cartItems.isEmpty()) throw IllegalArgumentException("cartItems is Empty")

            val order = Order(orderNumber = orderNumber,
                    member = member,
                    shippingAddress = address,
                    technical = technical,
                    remark = remark,
                    source = "")
            cartItems.forEach { order.addItem(it.product, it.quantity) }

            return order
        }
    }

}
