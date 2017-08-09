package core.ec.member.port.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/member")
class MemberController {

    @RequestMapping("user")
    fun user(user: Principal): Principal {
        return user
    }
}