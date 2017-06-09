package core.ec.order.common

import org.springframework.data.domain.AfterDomainEventPublication
import org.springframework.data.domain.DomainEvents
import javax.persistence.Transient

/**
 *
 */
open class EntityObject {
    @Transient
    protected val _events: MutableCollection<DomainEvent> = ArrayList<DomainEvent>()

    @DomainEvents
    fun domainEvents(): Collection<DomainEvent> = _events

    @AfterDomainEventPublication
    fun clearEvents() = _events.clear()
}