package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.service.TakeCourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminTakeCourseController extends BaseController{

    private final TakeCourseService takeCourseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/takecourse/list")
    public String list(Model model , TakeCourseParam param){

        param.init();
        List<TakeCourseDto> takCourseList = takeCourseService.list(param);

        long totalCount = 0;
        if(!CollectionUtils.isEmpty(takCourseList) ){
            totalCount = takCourseList.get(0).getTotalCount();
        }

        String QueryString = param.getQueryString(); // 조회 조건
        String pagerHtml = getPagerHtml(totalCount, param.getPageSize(), param.getPageIndex(), QueryString );

        model.addAttribute("pager", pagerHtml);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("list", takCourseList);
        return "admin/takecourse/list";
    }

    @PostMapping("/admin/takecourse/status")
    public String status(Model model , TakeCourseParam param){

        ServiceResult result = takeCourseService.updateStatus(param.getId(), param.getStatus());

        if (!result.isResult()){
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }
        return "redirect:/admin/takecourse/list";
    }

}
