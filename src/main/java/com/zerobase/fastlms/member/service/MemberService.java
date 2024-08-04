package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService{
    boolean register(MemberInput param);

    /**
     * uuid에 해당하는 계정을 활성화
     */
    boolean emailAuth(String uuid);


    /**
     * 
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     */
    boolean sendRestPassword(ResetPasswordInput param);

    /**
     * 입력받은 uuid에 대해 password로 초기화
     */
    boolean resetPassword(String uuid, String password);

    /**
     * 입력받은 uuid 값이 유효한지 확인
     */
    boolean checkResetPassword(String uuid);

    /**
     *회원목록리턴(관리자에서만 사용가능)
     */
    //    List<Member> list(); jap사용시
    List<MemberDto> list(MemberParam param);

    /**
     * 회원 상세 정보
     */
    MemberDto detail(String userId);

    /**
     * 회원 상태 변경
     */
    boolean updateStatus(String userId, String userStatus);

    /**
     * 회원 비밀번호 초기화(관리자)
     */
    boolean updatePassword(String userId, String password);

    /**
     *
     * 회원 비밀번호 초기화(사용자)
     */
    ServiceResult updateMemberPassword(MemberInput param);

    /**
     * 회원정보 수정
     *
     */
    ServiceResult updateMember(MemberInput param);


    /*
    회원 탈퇴
     */
    ServiceResult withdraw(String userId, String password);
}
