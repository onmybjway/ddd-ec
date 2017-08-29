package core.ec.member.application

import core.ec.member.domain.model.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class SecurityUserService(
        val memberRepository: MemberRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.getByMemberName(username)
                .map { m ->
                    //                    User.withUsername(m.memberName).password(m.password).roles("ENDUSER").build()
                    SecurityUserWithId(m.memberId, m.memberName, m.password, listOf(SimpleGrantedAuthority("ROLE_ENDUSER")))
                }
                .orElseThrow { UsernameNotFoundException(username) }
    }
}

class SecurityUserWithId(val userId: Long, username: String, password: String, authorities: Collection<GrantedAuthority>)
    : User(username, password, authorities)
