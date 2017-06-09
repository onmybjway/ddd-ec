package core.ec.port.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class Order(
        var orderNumber: String = "",
        var amount: Double = 0.00,
        var remark: String = "",
        @JsonFormat(timezone = "Asia/Shanghai", locale = "zh_CN")
        var createTime: Date = Date(),
        @JsonFormat(timezone = "Asia/Shanghai", locale = "zh_CN")
        var lastChangeTime: Date = Date(),
        var items: Iterable<OrderItem> = emptySet()

) {
    data class OrderItem(
            var productId: String = "",
            var quantity: Int = 0,
            var productName: String = "",
            var price: Double = 0.00,
            var subtitle: Double = 0.00
    )
}