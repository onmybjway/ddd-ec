package core.ec.domain.model

import core.ec.common.EnumWithStringKeyConvert
import core.ec.common.ValueObject
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Past

/**
 * 订单变更记录
 */
@Entity
class OrderChangeLog(
        @Column
        @Convert(converter = EnumWithStringKeyConvert::class)
        val fromStatus: OrderStatus,

        @Column
        @Convert(converter = EnumWithStringKeyConvert::class)
        val toStatus: OrderStatus

) : ValueObject() {

    @Id
    @GeneratedValue
    private val id: Long = 0

    val occurTime: Date = Date()

    private constructor() : this(OrderStatus.NULL, OrderStatus.NULL)
}