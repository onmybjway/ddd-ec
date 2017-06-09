package core.ec.common

/**
 *
 */
open class EntityObject {
    @javax.persistence.Transient
    protected val _events: MutableCollection<DomainEvent> = ArrayList<DomainEvent>()

    @org.springframework.data.domain.DomainEvents
    fun domainEvents(): Collection<DomainEvent> = _events

    @org.springframework.data.domain.AfterDomainEventPublication
    fun clearEvents() = _events.clear()
}