package core.ec.member

import core.ec.member.domain.model.Member
import core.ec.member.domain.model.MemberRepository
import core.ec.member.domain.model.MemberStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles

@Component
@ActiveProfiles("dev")
class InitDataOnDev : ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    lateinit var context: ApplicationContext

    override fun onApplicationEvent(event: ApplicationReadyEvent?) {
        val repository = context.getBean(MemberRepository::class.java)
        (1..10).forEach { repository.save(Member("member$it", MemberStatus.ACTIVE)) }
    }


}