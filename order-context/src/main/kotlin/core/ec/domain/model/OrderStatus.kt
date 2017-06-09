package core.ec.domain.model

import core.ec.common.EnumWithKey
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

/**
 * 订单状态 (枚举)
 */
@JsonSerialize(using = ToStringSerializer::class)
abstract class OrderStatus private constructor(key: String, value: String, display: String) : EnumWithKey(key, value, display) {

    open fun invalid(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun approved(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun paymentSuccess(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun paymentFailed(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun packageSent(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun received(order: Order) {
        throw UnsupportedOperationException()
    }

    open fun reject(order: Order) {

    }

    companion object {

        @JvmField
        val COMPLETED = object : OrderStatus("C", "COMPLETED", "完成") {}

        @JvmField
        val FAILED = object : OrderStatus("F", "FAILED", "失败") {}

        @JvmField
        val SHIPPING: OrderStatus = object : OrderStatus("S", "SHIPPING", "已发货") {
            override fun received(order: Order) {
                order.status = COMPLETED
            }

            override fun reject(order: Order) {
                order.status = FAILED
            }
        }

        @JvmField
        val PAID: OrderStatus = object : OrderStatus("P", "PAID", "已支付") {
            override fun invalid(order: Order) {
                order.status = FAILED
            }

            override fun approved(order: Order) {
                order.status = VALID
            }
        }

        @JvmField
        val VALID: OrderStatus = object : OrderStatus("V", "VALID", "有效订单") {

            override fun packageSent(order: Order) {
                order.status = SHIPPING
            }
        }

        @JvmField
        val NEW: OrderStatus = object : OrderStatus("N", "NEW", "新订单") {
            override fun paymentSuccess(order: Order) {
                order.status = PAID
            }

            override fun paymentFailed(order: Order) {
                order.status = FAILED
            }
        }

        @JvmField
        val NULL = object : OrderStatus("-", "NULL", "未设置"){}
    }


}


