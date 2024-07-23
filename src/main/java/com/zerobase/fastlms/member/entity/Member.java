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

    

}
