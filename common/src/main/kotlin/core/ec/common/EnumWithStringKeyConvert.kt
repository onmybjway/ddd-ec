package core.ec.common


@javax.persistence.Convert
class EnumWithStringKeyConvert : javax.persistence.AttributeConverter<EnumWithKey, String> {
    override fun convertToDatabaseColumn(attribute: EnumWithKey): String {
        return attribute.key
    }

    override fun convertToEntityAttribute(dbData: String): EnumWithKey {
        return EnumWithKey.formKey(dbData)
    }

}