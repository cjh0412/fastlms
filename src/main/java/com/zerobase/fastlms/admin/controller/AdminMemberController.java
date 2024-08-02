package com.zerobase.fastlms.admin.controller;


import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.admin.model.MemberInput;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminMemberController extends BaseController {
    private final MemberService memberService;

    @GetMapping("/admin/member/list")
    public String list(Model model, MemberParam param){

        param.init();
        List<MemberDto> members = memberService.list(param);

        long totalCount = 0;
        if(members != null && members.size() > 0){
            totalCount = members.get(0).getTotalCount();
        }

        String QueryString = param.getQueryString(); // 조회 조건
        String pagerHtml = getPagerHtml(totalCount, param.getPageSize(), param.getPageIndex(), QueryString );

        model.addAttribute("pager", pagerHtml);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("list", members);

        return "admin/member/list";
    }

    @GetMapping("/admin/member/detail")
    public String detail(Model model, MemberParam param){

        param.init();

        MemberDto member =
        memberService.detail(param.getUserId());

        model.addAttribute("member", member);

        return "admin/member/detail";
    }

    @PostMapping("/admin/member/status")
    public String status(Model model, MemberInput param){

        boolean result =
        memberService.updateStatus(param.getUserId(), param.getUserStatus());

        return "redirect:/admin/member/detail?userId=" + param.getUserId();
    }

    @PostMapping("/admin/member/password")
    public String password(Model model, MemberInput param){

        boolean result =
                memberService.updatePassword(param.getUserId(), param.getPassword());

        return "redirect:/admin/member/detail?userId=" + param.getUserId();
    }
}
