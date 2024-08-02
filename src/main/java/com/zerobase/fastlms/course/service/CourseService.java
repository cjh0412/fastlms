package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;

import java.util.List;

public interface CourseService {

    /**
     * 강좌등록
     */
    boolean add(CourseInput param);

    /*
        강좌목록
     */
    List<CourseDto> list(CourseParam param);

}
