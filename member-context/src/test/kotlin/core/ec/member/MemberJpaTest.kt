package core.ec.member

import core.ec.member.domain.model.Member
import core.ec.member.domain.model.MemberRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by user on 2017/6/14.
 */
@RunWith(SpringRunner::class)
@DataJpaTest
class MemberJpaTest {

    @Autowired
    lateinit private var entityManager: TestEntityManager

    @Autowired
    lateinit var repository: MemberRepository

    @Test
    fun name() {
        repository.save(Member("user1"))
    }
}