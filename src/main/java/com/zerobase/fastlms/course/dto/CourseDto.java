package com.zerobase.fastlms.course.dto;

import com.zerobase.fastlms.course.entity.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CourseDto {

    Long id;
    String imagePath;
    String keyword;
    String subject;
    String summary;
    String contents;
    long price;
    long salePrice;
    LocalDate saleEndDt;
    LocalDateTime regDt; // 등록일
    LocalDateTime udtDt; // 수정일

    // 추가컬럼
    long totalCount;
    long seq; // no

    long categoryId;



    public static CourseDto of(Course course) {
        return  CourseDto.builder()
                .id(course.getId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .subject(course.getSubject())
                .summary(course.getSummary())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .regDt(course.getRegDt())
                .udtDt(course.getUdtDt())
                .categoryId(course.getCategoryId())
                .build();

    }

    public static List<CourseDto> of(List<Course> courses) {

        if(courses == null ) {
            return null;
        }

        List<CourseDto> courseList = new ArrayList<>();
        for(Course course : courses) {
            courseList.add(CourseDto.of(course));
        }
        return courseList;

    }
}
