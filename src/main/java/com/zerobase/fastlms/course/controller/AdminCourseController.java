package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminCourseController extends BaseController{

    private final CourseService courseService;

    @GetMapping("/admin/course/list")
    public String list(Model model , CourseParam param){

        param.init();
        List<CourseDto> courseList = courseService.list(param);

        long totalCount = 0;
        if(CollectionUtils.isEmpty(courseList) && courseList.size() > 0){
            totalCount = courseList.get(0).getTotalCount();
        }

        String QueryString = param.getQueryString(); // 조회 조건
        String pagerHtml = getPagerHtml(totalCount, param.getPageSize(), param.getPageIndex(), QueryString );
//        PageUtil pageUtil = new PageUtil(totalCount, param.getPageSize(), param.getPageIndex(), QueryString); // baseController 이동

        model.addAttribute("pager", pagerHtml);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("list", courseList);
        return "admin/course/list";
    }

    @GetMapping("/admin/course/add")
    public String add(Model model){

        return "admin/course/add";
    }

    @PostMapping("/admin/course/add")
    public String addSubmit(Model model, CourseInput param){
        boolean result = courseService.add(param);

        return "redirect:/admin/course/list";
    }
}
