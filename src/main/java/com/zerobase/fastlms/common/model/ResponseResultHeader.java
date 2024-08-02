package com.zerobase.fastlms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class ResponseResultHeader {

    boolean result;
    String message;

    public ResponseResultHeader(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public ResponseResultHeader(boolean result) {
        this.result = result;
    }
}
