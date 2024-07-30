package com.zerobase.fastlms.member.model;

import lombok.Data;


@Data // getter, setter , 생성자 자동 생성
public class ResetPasswordInput {
    private String userId ;
    private String userName;

    private String id;
    private String password;
}
