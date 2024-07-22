package com.zerobase.fastlms.member.model;

import lombok.Data;


@Data // getter, setter , 생성자 자동 생성
public class MemberInput {
    private String userId ;
    private String userName;
    private String password ;
    private String phone ;
}
