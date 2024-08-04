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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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


    String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename){
        LocalDate now = LocalDate.now();

        String[] dirs = {
        //연 폴더
        String.format("%s/%d/", baseLocalPath, now.getYear()),

        // 연/월 폴더
        String.format("%s/%d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue()),

        //연/월/일 폴더
         String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())
        };

        String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for(String dir : dirs){
            //파일 생성
            File file = new File(dir);
            if(!file.isDirectory()){
                file.mkdir();
            }
        }

        String fileExtension = "";
        if(originalFilename != null){
            int dotPos = originalFilename.lastIndexOf("."); // 확장자 추출
            if(dotPos > -1){
                fileExtension = originalFilename.substring(dotPos + 1);
            }
        }


        String uuid = UUID.randomUUID().toString().replace("-",""); // uuid의 -제거
        String newFileName = String.format("%s%s", dirs[2], uuid); //파일명
        String newUrlFileName = String.format("%s%s", urlDir, uuid); //webapp에 설정된 파일 url 경로(/files부터 시작)
        if(fileExtension.length() > 0){
            newFileName += "." + fileExtension; // new 파일명+확장자
            newUrlFileName += "." + fileExtension; // new 파일명+확장자
        }

        return new String[]{newFileName, newUrlFileName};

    }


    @PostMapping(value={"/admin/course/add", "/admin/course/edit"})
    public String addSubmit(Model model,
                            CourseInput param,
                            HttpServletRequest request,
                            MultipartFile file){

        String saveFileName = "";
        String urlFileName = "";

        if(file != null){

            //업로드된 파일명+확장자
            String originalFilename = file.getOriginalFilename();

            //파일 업로드 경로 세팅
            String baseLocalPath = "C:/dev/zerobase/fastlms/files";
            String baseUrlPath = "/files";
            String[] arrFilename = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            saveFileName = arrFilename[0];
            urlFileName = arrFilename[1];


            try {
                File newFile = new File(saveFileName);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            }catch (Exception e){
                log.error(e.getMessage());
            }

        }

        param.setFileName(saveFileName);
        param.setUrlFileName(urlFileName);

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
