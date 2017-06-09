package core.ec.common

open class EnumWithKey(
        val key: String,
        val value: String,
        val description: String
) : java.io.Serializable {
    init {
        core.ec.common.EnumWithKey.Companion.add(this)
    }

    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as core.ec.common.EnumWithKey
        if (key != other.key) return false
        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    internal companion object {
        val enumHolder: MutableSet<core.ec.common.EnumWithKey> = HashSet()

        fun add(theEnum: core.ec.common.EnumWithKey) {
            core.ec.common.EnumWithKey.Companion.enumHolder.add(theEnum)
        }

        fun formKey(key: String): core.ec.common.EnumWithKey {
            return core.ec.common.EnumWithKey.Companion.enumHolder.firstOrNull({ itemLine -> itemLine.key == key })
                    ?: throw RuntimeException("do not match the key:$key")
        }
    }

}