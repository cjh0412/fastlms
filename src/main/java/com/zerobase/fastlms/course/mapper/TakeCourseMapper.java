package com.zerobase.fastlms.course.mapper;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.entity.TakeCourse;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {
    long selectListCount(TakeCourseParam Param);
    List<TakeCourseDto> selectList(TakeCourseParam Param);
    List<TakeCourseDto> selectListMyCourse(TakeCourseParam Param);
}
