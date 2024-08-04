package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    String userId;
    String password;
    String phone;
    LocalDateTime regDt;
    LocalDateTime udtDt;
    String userName;
    String emailAuthKey;
    boolean emailAuthYN;
    LocalDateTime emailAuthDt;

    String resetPasswordKey;
    LocalDateTime resetPasswordLimitDt;
    boolean adminYn;
    String userStatus;

    // 추가컬럼
    long totalCount;
    long seq; // no

    //주소관련
    private String zipcode;
    private String addr;
    private String addrDetail;

    public static MemberDto of (Member member) {
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .phone(member.getPhone())
                .regDt(member.getRegDt())
                .emailAuthYN(member.isEmailAuthYN())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthKey(member.getEmailAuthKey())
                .resetPasswordKey(member.getResetPasswordKey())
                .adminYn(member.isAdminYn())
                .userStatus(member.getUserStatus())
                .addr(member.getAddr())
                .zipcode(member.getZipcode())
                .addrDetail(member.getAddrDetail())
                .build();

    }

    public String getRegDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return regDt!= null ? regDt.format(formatter) : ""; // regDt가 null이 아닌 경우만

    }

    public String getUdtDtText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return udtDt!= null ? udtDt.format(formatter) : "";

    }

}
