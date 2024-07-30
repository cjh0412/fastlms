package com.zerobase.fastlms.configuration;


import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity // Spring security를 사용하기 위한 어노테이션
@RequiredArgsConstructor
//public class SecurityConfiguration  extends WebSecurityConfiguration{ // 상속 대신 bean 설정하여 진행
// extends 를 지우지 않을 경우 순환 참조 오류 발생
public class SecurityConfiguration {
    //    //유저정보전달
    private final MemberService memberService;

    //사용자 권한
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());
        return auth.build();
    }

    // 로그인 실패처리 메서드
    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    //비밀번호 암호화
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        // 예외처리하고 싶은 url
                        .requestMatchers("/"
                                , "/member/register"
                                , "/member/email-auth"
                                , "/member/login"
                                , "/member/password"
                                ,"/member/find/password"
                                ,"/member/reset/password")
                        .permitAll()
                        .requestMatchers("/admin/**")      // 관리자 페이지의 경우 권한 설정
                        .hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest()
                        .authenticated()

                );


        http.formLogin((form) -> form.loginPage("/member/login") // 로그인페이지
                .loginProcessingUrl("/member/loginProc")
                .failureUrl("/member/login?error=true")
                .failureHandler(getFailureHandler())

        );

        http.logout((logout)-> logout.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        ) // 세션 초기화
        ;
        http.exceptionHandling((exception) -> exception.accessDeniedPage("/error/denied")
                );

        http.csrf((csrf) -> csrf.disable()); // 위조방지 비활성화

        return http.build();
    }

}
