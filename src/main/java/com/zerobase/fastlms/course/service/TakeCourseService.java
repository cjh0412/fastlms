package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.*;

import java.util.List;

public interface TakeCourseService {


    /*
        수강목록
     */
    List<TakeCourseDto> list(TakeCourseParam param);
    
    /*
    수강 내용 상태 변경(완료, 취소)
     */
    ServiceResult updateStatus(long id, String status);

    /**
     * 수강 상세 정보
     */
    TakeCourseDto detail(long id);


    /*
    수강내역(사용자)
     */
    List<TakeCourseDto> myCourse(String userId);

    /**
     * 수강신청 취소 처리
     */
    ServiceResult cancel(long id);
}
