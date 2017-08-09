package core.ec.member

import core.ec.member.application.MemberUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.authentication.dao.ReflectionSaltSource
import org.springframework.security.authentication.dao.SaltSource
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.lang.reflect.Member


@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userDetailsService: MemberUserService

    override fun configure(auth: AuthenticationManagerBuilder){
//        auth.inMemoryAuthentication().withUser("user").password("pwd").roles("ENDUSER", "ADMIN")
/*        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(this.passwordEncoder())
        provider.setSaltSource(this.saltSource())
        provider.setUserDetailsService(this.userDetailsService)
        auth.authenticationProvider(provider)*/
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): StandardPasswordEncoder {
        return StandardPasswordEncoder("secret")
    }

}

@Configuration
@EnableAuthorizationServer
class AuthServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    lateinit var tokenStore: TokenStore

    @Autowired
    lateinit var accessTokenConverter: JwtAccessTokenConverter

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("app")
                .secret("secret")
                .authorizedGrantTypes("implicit", "authorization_code", "refresh_token", "password")
                .scopes("read")
//                .authorities("TRUSTED_CLIENT")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore).accessTokenConverter(accessTokenConverter).authenticationManager(authenticationManager)
    }
}

@Configuration
@EnableResourceServer
class ResourceConfig : ResourceServerConfigurerAdapter() {
    @Autowired
    lateinit var tokenStore: TokenStore

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.tokenServices(tokenService())
    }

    @Bean
    @Primary
    fun tokenService(): DefaultTokenServices {
        val ts = DefaultTokenServices()
        ts.setTokenStore(tokenStore)
        return ts
    }
}

@Configuration
class JwtConfig {
    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey("key")
        return converter
    }
}