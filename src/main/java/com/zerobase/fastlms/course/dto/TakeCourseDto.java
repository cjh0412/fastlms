package com.zerobase.fastlms.course.dto;

import com.zerobase.fastlms.course.entity.TakeCourse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TakeCourseDto{

    Long id;

    long courseId;
    String userId;

    long payPrice; // 결제금액
    String status; // 상태(수강신청, 결재완료, 수강취소)
    LocalDateTime regDt; // 신청일
    
    //join 데이터
    String userName;
    String phone;
    String subject;

    // 페이징
    long totalCount;
    long seq; // no

    public static TakeCourseDto of(TakeCourse x) {
        return TakeCourseDto.builder()
                .id(x.getId())
                .courseId(x.getCourseId())
                .userId(x.getUserId())
                .payPrice(x.getPayPrice())
                .status(x.getStatus())
                .regDt(x.getRegDt())
                .build();
    }

    public String getRegDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return regDt!= null ? regDt.format(formatter) : ""; // regDt가 null이 아닌 경우만

    }

}

