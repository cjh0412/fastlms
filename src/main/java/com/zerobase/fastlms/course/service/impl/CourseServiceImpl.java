package com.zerobase.fastlms.course.service.impl;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    /**
     * 강좌등록
     */
    @Override
    public boolean add(CourseInput param) {

        Course course = Course.builder()
                .subject(param.getSubjec())
                .regDt(LocalDateTime.now())
                .build();
        courseRepository.save(course);
        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam param) {

        long totalCount = courseMapper.selectListCount(param);

        List<CourseDto> list = courseMapper.selectList(param);

        int i =0;

        //CollectionUtils.isEmtpy : null 까지 체크함
        //list.isEmpty 는 null일 겨우 에러 발생
        if(!CollectionUtils.isEmpty(list)){
            for(CourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
    }
}
