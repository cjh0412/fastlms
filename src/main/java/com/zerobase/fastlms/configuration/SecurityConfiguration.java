package com.zerobase.fastlms.configuration;


import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// Spring security를 사용하기 위한 어노테이션

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
//public class SecurityConfiguration  extends WebSecurityConfiguration{ // 상속 대신 bean 설정하여 진행
// extends 를 지우지 않을 경우 순환 참조 오류 발생
public class SecurityConfiguration  {

    //유저정보전달
    private final MemberService memberService;

    // 로그인 실패처리 메서드
    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }


    //비밀번호 발급
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //사용자 권한
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    // 현재 WebSecurityConfigurationAdapter 사용 불가 SecurityFilterChain 대체
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()); // 위조방지 비활성화
        http
                .authorizeHttpRequests((requests) -> requests
                        // 예외처리하고 싶은 url
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/member/register").permitAll()
                        .requestMatchers("/member/email-auth").permitAll()
                        .requestMatchers("/member/password").permitAll()
                        // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
                                .anyRequest().authenticated()
                )
                .formLogin((form)-> form.loginPage("/member/login")
                                .failureHandler(getFailureHandler())
                                .permitAll()
                        )
        ;

//               .logout((logout) -> logout.logoutUrl("/member/logout").permitAll()
//                );

        System.out.println("test======================================================================");
        return http.build();
    }




}
