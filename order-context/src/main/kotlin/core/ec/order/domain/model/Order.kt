package core.ec.order.domain.model

import core.ec.common.EntityObject
import core.ec.common.EnumWithStringKeyConvert
import core.ec.order.domain.event.OrderCreatedEvent
import core.ec.order.domain.event.OrderStatusChangedEvent
import core.ec.order.exceptions.ProductOutOfStockException
import org.hibernate.validator.constraints.NotBlank
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.persistence.*
import javax.validation.constraints.DecimalMin
import kotlin.collections.HashSet

/**
 * 订单
 */
@Entity
@Table(name = "orders")
class Order private constructor() : EntityObject() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Version
    val version: Int = 0

    @Column(length = 20, unique = true)
    @NotBlank
    var orderNumber: String = ""
        private set(orderNumber) {
            field = if (orderNumber.isEmpty()) throw IllegalArgumentException("orderNumber") else orderNumber
        }

    @Embedded
    lateinit var member: Member
        private set

    @Column(length = 200)
    var remark: String = ""

    //    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    @Convert(converter = EnumWithStringKeyConvert::class)
    var status: OrderStatus = OrderStatus.NULL

    val createTime = Date()

    var lastChangeTime = Date()
        private set

    @Embedded
    lateinit var shippingAddress: ShippingAddress
        private set

    @Column(scale = 2)
    @DecimalMin("0")
    private var amount = BigDecimal.ZERO
        private set(v) {
            field = if (v.scale() == 2) v else v.setScale(2, RoundingMode.HALF_UP)
        }

    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "order_id")
    //    @Valid
    private val _items: MutableSet<OrderItem> = HashSet()

    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "order_id")
    private val _logs: MutableSet<OrderChangeLog> = HashSet()


    internal constructor(orderNumber: String, member: Member, shippingAddress: ShippingAddress, remark: String = "") : this() {
        this.orderNumber = orderNumber
        this.member = member
        this.shippingAddress = shippingAddress
        this.remark = remark
        this.status = OrderStatus.NEW

        this._events.add(OrderCreatedEvent(orderNumber))
        this._logs.add(OrderChangeLog(fromStatus = OrderStatus.NULL, toStatus = OrderStatus.NEW))

    }

    fun getAmount(): Double = amount.toDouble()

    fun getItems(): Iterable<OrderItem> = _items

    fun getLogs(): Iterable<OrderChangeLog> = _logs

    /**
     * add new item into order

     * @param product  product
     * *
     * @param quantity quantity of product
     */
    fun addItem(product: Product, quantity: Int) {

        if (product.inStock < quantity) throw ProductOutOfStockException(product.productId)

        val newItem = OrderItem(product, quantity)
        // check duplicate
        if (this._items.contains(newItem)) {
            throw RuntimeException("do not add duplicate product")
        }

        // put in enumHolder
        this._items.add(newItem)

        // update amount
        this.amount = this._items.fold(BigDecimal.ZERO) { acc, orderItem -> acc + orderItem.subtitle }
    }

    fun invalid() = this.trackingStatus { this.status.invalid(this) }

    fun approved() = this.trackingStatus { this.status.approved(this) }

    fun paymentSuccess() = this.trackingStatus { this.status.paymentSuccess(this) }

    fun unpaid() = this.trackingStatus { this.status.paymentFailed(this) }

    fun packageSent() = this.trackingStatus { this.status.packageSent(this) }

    fun received() = this.trackingStatus { this.status.received(this) }

    fun reject() = this.trackingStatus { this.status.reject(this) }

    private fun trackingStatus(toChange: () -> Unit) {
        val beforStatus = this.status
        toChange()
        //record log
        this._logs.add(OrderChangeLog(fromStatus = beforStatus, toStatus = this.status))
        //trigger event(record)
        this._events.add(OrderStatusChangedEvent(this.orderNumber, beforStatus, changeTo = this.status))

        this.lastChangeTime = Date()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Order

        if (orderNumber != other.orderNumber) return false

        return true
    }

    override fun hashCode(): Int {
        return orderNumber.hashCode()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Order::class.java)
    }
}