package core.ec.member.port.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class MemberSummary(
        var memberId: Long = 0,
        var memberName: String = "",

        @JsonFormat(timezone = "Asia/Shanghai", locale = "zh_CN")
        var regDate: Date = Date(),

        @JsonFormat(timezone = "Asia/Shanghai", locale = "zh_CN")
        var lastAuthDate: Date = Date()

) {

}