package it.polito.wa2.g07.travelerservice.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  private val unauthorizedHandler: AuthEntryPointJwt? = null

  @Bean
  fun authenticationJwtTokenFilter(): JwtAuthenticationTokenFilter? {
    return JwtAuthenticationTokenFilter()
  }

  override fun configure(http: HttpSecurity) {
    http.httpBasic().disable()
        .cors()
      .and()
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
      .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
      .and()
        .authorizeRequests()
            .mvcMatchers("/my/**").hasAnyRole("CUSTOMER","ADMIN")
            .mvcMatchers("/admin/**").hasRole("ADMIN")
            .mvcMatchers("/secret").hasAnyRole("EMBEDDEDSYSTEM","ADMIN")
        .anyRequest().authenticated()
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder? {
    return BCryptPasswordEncoder()
  }

}
