package com.zerobase.fastlms.member.entity;

public interface MemberCode {
    
    //이용중인 상태
    String MEMBER_STATUS_ING = "ING";
    
    // 정지 상태
    String MEMBER_STATUS_STOP = "STOP";

    //가입 요청 중
    String MEMBER_STATUS_REQ = "REQ";

    //회원 탈퇴
    String MEMBER_STATUS_WITHDRAW = "WITHDRAW";
}
