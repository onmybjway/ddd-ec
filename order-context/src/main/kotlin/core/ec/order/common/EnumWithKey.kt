package core.ec.order.common

import java.io.Serializable

open class EnumWithKey(
        val key: String,
        val value: String,
        val description: String
) : Serializable {
    init {
        add(this)
    }

    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as EnumWithKey
        if (key != other.key) return false
        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    internal companion object {
        val enumHolder: MutableSet<EnumWithKey> = HashSet()

        fun add(theEnum: EnumWithKey) {
            enumHolder.add(theEnum)
        }

        fun formKey(key: String): EnumWithKey {
            return enumHolder.firstOrNull({ itemLine -> itemLine.key == key })
                    ?: throw RuntimeException("do not match the key:$key")
        }
    }

}