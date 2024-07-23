package com.zerobase.fastlms.configuration;


import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// Spring security를 사용하기 위한 어노테이션

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
//public class SecurityConfiguration  extends WebSecurityConfiguration{ // 상속 대신 bean 설정하여 진행
// extends 를 지우지 않을 경우 순환 참조 오류 발생
public class SecurityConfiguration  {

    //유저정보전달
    private final MemberService memberService;
    private final PasswordEncoderConfig passwordEncoder;

//    @Bean
//    UserAuthenticationFailurehandler getUserAuthenticationFailurehandler() {
//        return new UserAuthenticationFailurehandler();
//    }

    
    // 현재 WebSecurityConfigurationAdapter 사용 불가 SecurityFilterChain 대체
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("test!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.info("test@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");


        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/","/member/register" ,"/member/email-auth")
                        .permitAll()// 예외처리하고 싶은 url
                        .anyRequest()
                        .authenticated()
        )
                .formLogin((form)-> form.loginPage("/member/login").permitAll())
//               .logout((logout) -> logout.logoutUrl("/member/logout").permitAll()
//                );
        ;

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.memberService);
        provider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        return provider;
    }


    //현재 사용 불가(WebSecurityConfigurationAdapter사용시 가능)
//    @Override
    // 로그인 필요부분 권한 설정
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/","/member/register","/member/email-auth")
//                .permitAll();
//        super.configure(http);}
    
    //로그인 관련 부분
//        http.formLogin()
//                .loginPage("/member/login")
//                .failureHandler(null)
//                .permitAll();


}
