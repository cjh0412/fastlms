package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.util.PageUtil;
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
public class AdminCourseController extends BaseController{

    private static final Logger log = LoggerFactory.getLogger(AdminCourseController.class);
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/course/list")
    public String list(Model model , CourseParam param){

        param.init();
        List<CourseDto> courseList = courseService.list(param);

        long totalCount = 0;
        if(!CollectionUtils.isEmpty(courseList) ){
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

    @GetMapping(value={"/admin/course/add", "/admin/course/edit"})
    public String add(Model model, HttpServletRequest request, CourseInput param){
        
        //카테고리 정보

        model.addAttribute("category", categoryService.list());
        
        boolean editMode = request.getRequestURI().contains("/edit");
        CourseDto detail = new CourseDto();
        if(editMode){
            long id = param.getId();
            CourseDto existCourse = courseService.getById(id);
            if(existCourse == null){
                //error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("detail", detail);
        model.addAttribute("editMode", editMode);


        return "admin/course/add";
    }

    @PostMapping(value={"/admin/course/add", "/admin/course/edit"})
    public String addSubmit(Model model, CourseInput param, HttpServletRequest request){
        boolean editMode = request.getRequestURI().contains("/edit");
        if(editMode){
            long id = param.getId();
            CourseDto existCourse = courseService.getById(id);
            if(existCourse == null){
                //error 처리
                model.addAttribute("message", "강좌 정보가 존재하지 않습니다.");
                return "common/error";
            }
            boolean result = courseService.set(param); // 수정전용
        }else {
            boolean result = courseService.add(param);
        }

        return "redirect:/admin/course/list";
    }

    @PostMapping("/admin/course/delete")
    public String del(Model model, CourseInput param, HttpServletRequest request){

        boolean result = courseService.del(param.getIdList());

        return "redirect:/admin/course/list";
    }
}
