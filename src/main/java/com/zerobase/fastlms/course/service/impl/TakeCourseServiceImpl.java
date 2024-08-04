package com.zerobase.fastlms.course.service.impl;

import com.zerobase.fastlms.admin.mapper.MemberMapper;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.entity.TakeCourse;
import com.zerobase.fastlms.course.entity.TakeCourseCode;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.mapper.TakeCourseMapper;
import com.zerobase.fastlms.course.model.*;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.repository.TakeCourseRepository;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TakeCourseServiceImpl implements TakeCourseService {

    private final TakeCourseMapper takeCourseMapper;
    private final TakeCourseRepository TakeCourseRepository;
    private final TakeCourseRepository takeCourseRepository;

    @Override
    public List<TakeCourseDto> list(TakeCourseParam param) {

        long totalCount = takeCourseMapper.selectListCount(param);

        List<TakeCourseDto> list = takeCourseMapper.selectList(param);

        int i =0;

        //CollectionUtils.isEmtpy : null 까지 체크함
        //list.isEmpty 는 null일 겨우 에러 발생
        if(!CollectionUtils.isEmpty(list)){
            for(TakeCourseDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public ServiceResult updateStatus(long id, String status) {
        Optional<TakeCourse> optionalTakeCourse = TakeCourseRepository.findById(id);
        if(!optionalTakeCourse.isPresent()){
            return new ServiceResult(false, "수강정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);
        return new ServiceResult(true);
    }

    /**
     * 수강 상세 정보
     *
     * @param id
     */
    @Override
    public TakeCourseDto detail(long id) {

        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (optionalTakeCourse.isPresent()) {
            return TakeCourseDto.of(optionalTakeCourse.get());
        }
        return null;
    }

    @Override
    public List<TakeCourseDto> myCourse(String userId) {
        TakeCourseParam param = new TakeCourseParam();
        param.setUserId(userId);

        List<TakeCourseDto> list = takeCourseMapper.selectListMyCourse(param);

        return list;
    }

    /**
     * 수강신청 취소 처리
     *
     * @param id
     */
    @Override
    public ServiceResult cancel(long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (!optionalTakeCourse.isPresent()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }

        TakeCourse takeCourse = optionalTakeCourse.get();

        takeCourse.setStatus(TakeCourseCode.STATUS_CANCEL);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult();
    }
}
