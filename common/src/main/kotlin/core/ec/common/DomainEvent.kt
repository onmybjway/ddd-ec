package core.ec.common

import java.util.*

/**
 *
 */
open class DomainEvent(
        val occurTime: Date = Date(),
        val eventId: String = UUID.randomUUID().toString()
) : ValueObject()