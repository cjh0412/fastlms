package com.zerobase.fastlms.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Member {
    @Id // pk
    private String userId;

    private String userName;
    private String password ;
    private String phone ;
    private LocalDateTime regDt;
}
