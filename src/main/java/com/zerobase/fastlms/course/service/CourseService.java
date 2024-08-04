package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {

    /*
     * 강좌등록
     */
    boolean add(CourseInput param);

    /*
    강좌정보수정
     */
    boolean set(CourseInput param);

    /*
        강좌목록
     */
    List<CourseDto> list(CourseParam param);

    /*
    강좌 상세 정보
     */
    CourseDto getById(long id);


    /*
    강좌 내용 삭제
     */
    boolean del(String idList);

    /*
     * 프론트 강좌 목록
     */
    List<CourseDto> frontList(CourseParam param);
    
    /*
    프론트 강좌 상세 정보
     */
    CourseDto frontDetail(long id);

    /*
    수강신청(사용자)
     */
    ServiceResult req(TakeCourseInput param);

    /*
    전체 강좌 목록
     */
    List<CourseDto> listAll();
}
