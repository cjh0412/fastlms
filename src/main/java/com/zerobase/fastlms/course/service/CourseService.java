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
}
