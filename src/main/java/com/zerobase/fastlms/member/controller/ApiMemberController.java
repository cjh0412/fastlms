package com.zerobase.fastlms.member.controller;


import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor // 생성자 자동 생성
@RestController
public class ApiMemberController {

    private static final Logger log = LoggerFactory.getLogger(ApiMemberController.class);
    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @PostMapping(value = "/api/member/course/cancel.api" )
    public ResponseEntity<?> cancelCourse(Model model
            , @RequestBody TakeCourseInput param
            , Principal principal) {

        String userId = principal.getName();
        TakeCourseDto detail = takeCourseService.detail(param.getTakeCourseId());
        if(detail == null) {
            ResponseResult responseResult = new ResponseResult(false, "수강 신청 정보가 존재하지 않습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        if(userId == null && !userId.equals(detail.getUserId())) {
            ResponseResult responseResult = new ResponseResult(false, "본인의 수강 신청 정보만 취소할 수 있습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        ServiceResult result = takeCourseService.cancel(param.getTakeCourseId());

        if(!result.isResult()){
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);

    }


}

