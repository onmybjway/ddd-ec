package core.ec.member

import core.ec.member.domain.model.Member
import core.ec.member.domain.model.MemberRepository
import core.ec.member.domain.model.MemberStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.stereotype.Component


@Component
class InitDataOnDev : ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    lateinit var context: ApplicationContext

    override fun onApplicationEvent(event: ApplicationReadyEvent?) {
        val repository = context.getBean(MemberRepository::class.java)
        val encoder = StandardPasswordEncoder("secret")
        (1..10).forEach {
            repository.save(
                    Member(memberName = "member$it",
                            password = encoder.encode("pwd"),
                            status = MemberStatus.ACTIVE
                    )
            )
        }

    }


}