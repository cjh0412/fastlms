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

}
