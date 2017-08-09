package core.ec.member.application

import core.ec.member.domain.model.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class MemberUserService(
        val memberRepository: MemberRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.getByMemberName(username)
                .map { m ->
                    User(m.memberName, m.password, setOf(SimpleGrantedAuthority("ENDUSER")))
                }
                .orElseThrow { UsernameNotFoundException(username) }
    }
}