package core.ec.domain.event

import core.ec.common.DomainEvent
import core.ec.domain.model.OrderStatus

/**
 *
 */
class OrderCreatedEvent(val orderNumber: String) : DomainEvent()

/**
 *
 */
class OrderStatusChangedEvent(
        val orderNumber: String,
        val changeFrom: OrderStatus,
        val changeTo: OrderStatus
) : DomainEvent()
