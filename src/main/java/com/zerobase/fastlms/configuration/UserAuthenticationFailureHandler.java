package com.zerobase.fastlms.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;


public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {

        String msg = "로그인에 실패하였습니다.";

        HttpSession session = request.getSession();
        if (exception instanceof InternalAuthenticationServiceException) {
            msg = exception.getMessage();
        }

        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        session.setAttribute("msg", msg);
        System.out.println(msg);

       super.onAuthenticationFailure(request,response,exception);



    }
}
