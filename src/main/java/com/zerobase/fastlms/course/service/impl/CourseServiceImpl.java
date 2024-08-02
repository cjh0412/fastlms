package com.zerobase.fastlms.course.service.impl;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.service.CourseService;
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
public class CourseServiceImpl implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식 설정
        try{
            return LocalDate.parse(date, fomatter);
        }catch (Exception e){
        }
        return null;
    }


    /**
     * 강좌등록
     */
    @Override
    public boolean add(CourseInput param) {

        LocalDate saleEndDt = getLocalDate(param.getSaleEndDtText());

        Course course = Course.builder()
                .subject(param.getSubject())
                .regDt(LocalDateTime.now())
                .categoryId(param.getCategoryId())
                .keyword(param.getKeyword())
                .price(param.getPrice())
                .summary(param.getSummary())
                .contents(param.getContents())
                .salePrice(param.getSalePrice())
                //종료일문자열
                .saleEndDt(saleEndDt)
                .build();
        courseRepository.save(course);
        return true;
    }

    @Override
    public boolean set(CourseInput param) {
        Optional<Course> courseOptional = courseRepository.findById(param.getId());
        LocalDate saleEndDt = getLocalDate(param.getSaleEndDtText());

        if(!courseOptional.isPresent()){
            //수정할 데이터가 존재하지 않음
            return false;
        }

        Course course = courseOptional.get();
        course.setCategoryId(param.getCategoryId());
        course.setSubject(param.getSubject());
        course.setUdtDt(LocalDateTime.now());
        course.setKeyword(param.getKeyword());
        course.setPrice(param.getPrice());
        course.setSummary(param.getSummary());
        course.setSalePrice(param.getSalePrice());
        course.setContents(param.getContents());
        course.setSaleEndDt(saleEndDt);
        //종료문자열

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

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);
    }

    @Override
    public boolean del(String idList) {
        if(idList != null || idList.length() > 0){

            String[] ids = idList.split(",");
            long id = 0L;
            for(String x : ids){
                try {
                    id = Long.parseLong(x);
                }catch (Exception e){

                }

                if(id > 0){
                    courseRepository.deleteById(id);
                }
            }

        }
        return true;
    }


}
