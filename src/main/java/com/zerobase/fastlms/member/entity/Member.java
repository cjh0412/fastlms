package com.zerobase.fastlms.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor // default 생성자
@AllArgsConstructor // 모든 파라메터가 존재하는 생성자
public class Member {
    @Id // pk
    private String userId;

    private String userName;
    private String password ;
    private String phone ;
    private LocalDateTime regDt;

    private boolean emailAuthYN; // 이메일 인증 여부
    private String emailAuthKey; // 이메일 인증 키
    private LocalDateTime emailAuthDt; // 이메일 인증 일자

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt; // 비밀번호 변경 유효 기관

    //관리자 여부를 지정?
    // 회원에 따른 ROLE 지정 중 선택
    // ex) 준회원, 정회원, 특별회원, 관리자 등 회원레벨을 부여하여 관리
    
    private boolean adminYn; // 관리자 여부 체크

    



    

}
