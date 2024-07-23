package com.zerobase.fastlms.member.controller;


import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor // 생성자 자동 생성
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/member/register" )
    public String register(){
        return "member/register";
    }

    @PostMapping(value = "/member/register")
    public String registerSumbmit(Model model, HttpServletResponse response
            , HttpServletRequest request
            , MemberInput parameter){

        System.out.println(parameter.toString());

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);
        return "member/register-complete";
    }

    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request){
        // 파라메터 받는 방법 : 1. request를 이용하여 전달받음
        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);
        return "member/email_auth";
    }

    @GetMapping("/member/info")
    public String memberInfo(){
        return "member/info";
    }

    @RequestMapping("/member/login")
    public String login(){
        return "member/login";
    }

}
