package com.zerobase.fastlms.course.model;

import lombok.Data;

//서비스 결과를 return
@Data
public class ServiceResult {

    boolean result;
    String message; //result 값이 false 일때

    public ServiceResult() {

    }

    public ServiceResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public ServiceResult(boolean result) {
        this.result = result;
    }
}
