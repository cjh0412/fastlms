package com.zerobase.fastlms.course.model;

import lombok.Data;

//서비스 결과를 return
@Data
public class ServiceResult {

    boolean result;
    String message; //result 값이 false 일때
}
