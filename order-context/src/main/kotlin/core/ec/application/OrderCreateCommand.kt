package core.ec.application

data class OrderCreateCommand(
        var memberId: Long,
        var address: ShippingAddress,
        var cartItems: Set<CartItem>,
        val remark: String
) {
    data class CartItem(
            val productId: String,
            val price:Double,
            val quantity: Int
    )

    data class ShippingAddress(
            val province: String,
            val city: String,
            val district: String,
            val address: String,
            val zipCode: String,
            val contactNumber: String,
            val receiver: String
    )
}