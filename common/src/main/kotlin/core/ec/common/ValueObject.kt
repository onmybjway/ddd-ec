package core.ec.common

open class ValueObject {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        return org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals(this, other);
    }

    override fun hashCode(): Int {
        return org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode(this)
    }
}
