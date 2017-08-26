package core.ec.member.port.rest

import core.ec.common.modelMapper
import core.ec.member.application.MemberService
import core.ec.member.port.dto.MemberSummary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/member")
class MemberController {
    @Autowired
    lateinit var memberService: MemberService

    @RequestMapping("user")
    fun user(user: Principal): Principal {
        return user
    }

    @RequestMapping("summary")
    fun summary(principal: Principal): MemberSummary {
        Thread.sleep(3000)
        return this.memberService.getByMemberName(principal.name)
                .map { member -> modelMapper.map(member, MemberSummary::class.java) }
                .orElseThrow { Exception("not found") }
    }
}