package com.zerobase.fastlms.course.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;

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
    LocalDateTime salEndDt;
    LocalDateTime regDt; // 등록일
    LocalDateTime udtDt; // 수정일

    // 추가컬럼
    long totalCount;
    long seq; // no
}
