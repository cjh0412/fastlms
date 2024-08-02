package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CourseController extends BaseController{

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String list(Model model
            // courseParam or @requestParam 중 하나만 사용
            //, @RequestParam(name="categoryId") long categoryId
            , CourseParam param){
        List<CourseDto> list = courseService.frontList(param); // 강좌 목록 조회
        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build()); // 카테고리 목록 조회

        //카테고리 전체 개수(각 카테고리별 강좌 개수 합계)
        int courseTotalCount = 0;
        if(!CollectionUtils.isEmpty(categoryList)){
            for(CategoryDto x : categoryList){
                courseTotalCount += x.getCourseCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", list);
        model.addAttribute("courseTotalCount", courseTotalCount);
        return "course/index";
    }


    @GetMapping("/course/{id}")
    public String detail(Model model, CourseParam param){

        CourseDto detail = courseService.frontDetail(param.getId());
        model.addAttribute("detail", detail);
        return "course/detail";
    }

}
