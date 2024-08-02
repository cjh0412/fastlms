package com.zerobase.fastlms.member.controller;


import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor // 생성자 자동 생성
@Controller
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    @GetMapping(value = "/member/register" )
    public String register(){
        return "member/register";
    }

    @PostMapping(value = "/member/register")
    public String registerSumbmit(Model model, HttpServletResponse response
            , HttpServletRequest request
            , MemberInput param){

        System.out.println(param.toString());

        boolean result = memberService.register(param);
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

    @RequestMapping("/member/loginProc")
    public String loginProc(){
        return "member/login";
    }

    @GetMapping("/member/find/password")
    public String findPassword(){
        return "member/find_password";
    }

    @PostMapping("/member/find/password")
    public String findPasswordSubmit(ResetPasswordInput param, Model model){
        boolean result = false;
        try{
            result = memberService.sendRestPassword(param);

        }catch (Exception e){
        }
        model.addAttribute("result", result);


//        return "redirect:/"; //주소와 view를 동일하게 변경하기 위해 redirect 사용

        return "member/find_password_result";
    }

    @GetMapping("/member/reset/password")
    public String resetPassword(HttpServletRequest request, Model model){
        String uuid = request.getParameter("id");
        boolean result = memberService.checkResetPassword(uuid);

        model.addAttribute("result", result);
        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(ResetPasswordInput param, Model model){
        boolean result = false;
        try{
            result = memberService.resetPassword(param.getId(), param.getPassword());

        }catch (Exception e){
        }

        model.addAttribute("result", result);
        return "member/reset_password_result";
    }
}

