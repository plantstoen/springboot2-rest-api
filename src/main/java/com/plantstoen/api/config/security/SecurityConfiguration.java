package com.plantstoen.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
리소스 접근 제한 표현식의 종류
hasIpAddress(ip) - 접근자의 IP주소가 매칭하는지 확인
hasRole(role) - 역할이 부여된 권한(Granted Authority)와 일치하는지 확인
hasAnyRole(role) - 부여된 역할 중 일치하는 항목이 있는지 확인
permitAll - 모든 접근자를 항상 승인
denyAll - 모든 사용자의 접근을 거부
anonymous - 사용자가 익명 사용자인지 확인
authenticated - 인증된 사용자인지 확인
rememberMe - 사용자가 remember me를 사용해 인증했는지 확인
fullyAuthenticated - 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인
*/

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() // rest api이므로 기본설정 사용 안함. 비인증시 로그인폼 화면으로 리다이렉트
                .csrf().disable() // rest api이므로 csrf 보안이 필요없음. 따라서 disable 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT token으로 인증하므로 세션은 필요없음
                .and()
                .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
                .antMatchers("/*/signin", "/*/signup").permitAll() // 가입 및 인증 주소
                .antMatchers(HttpMethod.GET, "helloworld/**").permitAll() // helloworld로 시작하는 GET요청 리소스
                .anyRequest().hasRole("USER") // 그외 나머지 요청은 인증된 회원만 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Override // swagger 페이지에 대해서는 예외를 적용
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}
