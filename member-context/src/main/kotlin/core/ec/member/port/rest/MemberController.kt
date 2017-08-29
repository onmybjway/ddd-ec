package core.ec.member.port.rest

import core.ec.common.NotFoundException
import core.ec.common.modelMapper
import core.ec.member.application.MemberService
import core.ec.member.port.dto.MemberSummary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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

    @RequestMapping("{memberId}/summary")
    @PreAuthorize("hasRole('ENDUSER') or (hasRole('ADMIN') and #oauth2.hasScope('read'))")
    @PostAuthorize("returnObject.memberName == authentication.name")
    fun summary(@PathVariable memberId: Long, principal: Principal): MemberSummary {
        return this.memberService.getByMemberId(memberId)
                .map { member ->
                    //                    if (member.memberName != principal.name) throw Exception("403")
                    modelMapper.map(member, MemberSummary::class.java)
                }
                .orElseThrow { NotFoundException("member:${memberId} was not found")}
    }
}