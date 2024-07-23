package com.zerobase.fastlms.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class UserAuthenticationFailurehandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        request.setAttribute("errorMessage","로그인실패");
        System.out.println("로그인 실패");

        super.onAuthenticationFailure(request, response, exception);
    }
}
