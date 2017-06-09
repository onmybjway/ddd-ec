package core.ec.common

import javax.persistence.AttributeConverter
import javax.persistence.Convert


@Convert
class EnumWithStringKeyConvert : AttributeConverter<EnumWithKey, String> {
    override fun convertToDatabaseColumn(attribute: EnumWithKey): String {
        return attribute.key
    }

    override fun convertToEntityAttribute(dbData: String): EnumWithKey {
        return EnumWithKey.formKey(dbData)
    }

}