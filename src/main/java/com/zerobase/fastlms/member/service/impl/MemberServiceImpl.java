package com.zerobase.fastlms.member.service.impl;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.mapper.MemberMapper;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.compoents.MailCompoents;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.exception.MemberNotEmailAuthException;
import com.zerobase.fastlms.member.exception.MemberStopUserException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailCompoents mailCompoents;
    private final MemberMapper memberMapper;


    /**
     * 회원가입
     */
    @Override
    public boolean register(MemberInput param) {

        Optional<Member> optionalMember =
                memberRepository.findById(param.getUserId()); //id를 가지고 메서드가 존재하는지 확인
        if (optionalMember.isPresent()) {// 동일 id 존재한다면
            return false;
        }
        String enPassword = BCrypt.hashpw(param.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(param.getUserId())
                .userName(param.getUserName())
                .phone(param.getPhone())
                .password(enPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYN(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STATUS_REQ) // 요청중 상태
                .build();

//        Member member = new Member();
//        member.setUserId(param.getUserId());
//        member.setUserName(param.getUserName());
//        member.setPassword(param.getPassword());
//        member.setPhone(param.getPhone());
//        member.setRegDt(LocalDateTime.now());
//        member.setEmailAuthYN(false);
//        member.setEmailAuthKey(UUID.randomUUID().toString()); // 랜덤값 생성

        memberRepository.save(member);

        String email = param.getUserId();
        String subject = "fastlms  사이트 가입을 축하드립니다.";
        String text = "<p>fastlms  사이트 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
                "<p><a href='http://localhost:8080/member/email-auth?id=" + uuid + "'>가입완료</a></p>";
        mailCompoents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {

        Optional<Member> optionalMember =  memberRepository.findByEmailAuthKey(uuid);

        if (!optionalMember.isPresent()) {
            return false;
        }
        Member member = optionalMember.get();

        // 이메일 확인 여부 체크
        if(member.isEmailAuthYN()) {
            return false; // 활성화 된 경우
        }

        member.setEmailAuthYN(true);
        member.setEmailAuthDt(LocalDateTime.now());
        member.setUserStatus(Member.MEMBER_STATUS_ING); // 이메일 인증 후 상태 변경(진행중)
        memberRepository.save(member);

        return true;
    }

    /**
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     *
     * @param param
     */
    @Override
    public boolean sendRestPassword(ResetPasswordInput param) {
        // 이메일, 이름 둘다 체크
        Optional<Member> optionalMember =
                memberRepository.findByUserIdAndUserName(param.getUserId(), param.getUserName());

        Member member = optionalMember.get();
        String uuid = UUID.randomUUID().toString();
        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1)); // 유효기관 하루
        memberRepository.save(member); // 데이터 저장
        
        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }
        String email = param.getUserId();
        String subject = "[fastlms] 비밀번호 초기화 메일";
        String text = "<p>fastlms 비밀번호 초기화 메일 입니다</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요</p>" +
                "<p><a href='http://localhost:8080/member/reset/password?id=" + uuid + "'>초기화링크</a></p>";
        mailCompoents.sendMail(email, subject, text);
        return true;
    }

    /**
     * 입력받은 uuid에 대해 password로 초기화
     *
     * @param uuid
     * @param password
     */
    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid); // email

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }


        Member member = optionalMember.get();

        //초기화 날짜가 유효한지 체크
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey(""); // 초기화 완료후 데이터 삭제
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    /**
     * 입력받은 uuid 값이 유효한지 확인
     *
     * @param uuid
     */
    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid); // email

        if (!optionalMember.isPresent()) {
            return false;
        }

        Member member = optionalMember.get();

        //초기화 날짜가 유효한지 체크
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }
        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        return true;
    }

    /**
     * 회원목록리턴(관리자에서만 사용가능)
     */
    @Override
    public List<MemberDto> list(MemberParam param) {
//        MemberDto param = new MemberDto();

        long totalCount = memberMapper.selectListCount(param);

        List<MemberDto> list = memberMapper.selectList(param);

        int i =0;

        //CollectionUtils.isEmtpy : null 까지 체크함
        //list.isEmpty 는 null일 겨우 에러 발생
        if(!CollectionUtils.isEmpty(list)){
            for(MemberDto x : list){
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - param.getPageStart() - i);
                i++;
            }
        }

        return list;
//        return memberRepository.findAll();
    }

    /**
     * 회원 상세 정보
     *
     * @param userId
     */
    @Override
    public MemberDto detail(String userId) {
        //JPA 이용
        Optional<Member> optionalMember = memberRepository.findById(userId);

        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();


        return MemberDto.of(member);

    }

    /**
     * 회원 상태 변경
     *
     * @param userId
     * @param userStatus
     */
    @Override
    public boolean updateStatus(String userId, String userStatus) {
        //회원 정보
        Optional<Member> optionalMember = memberRepository.findById(userId); // email

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

    /**
     * 회원 비밀번호 초기화
     *
     * @param userId
     * @param password
     */
    @Override
    public boolean updatePassword(String userId, String password) {
        //회원 정보
        Optional<Member> optionalMember = memberRepository.findById(userId); // email

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        //비밀번호 암호화
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        member.setPassword(encPassword);
        memberRepository.save(member);
        return true;
    }

    /**
     * 회원 비밀번호 초기화(사용자)
     *
     * @param param
     */
    @Override
    public ServiceResult updateMemberPassword(MemberInput param) {
        //회원 정보
        String userId = param.getUserId();
        Optional<Member> optionalMember = memberRepository.findById(userId); // email
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        //비밀번호 암호화

        if(!BCrypt.checkpw(param.getPassword(), member.getPassword())){
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다..");
        }

        String encPassword = BCrypt.hashpw(param.getNewPassword(), BCrypt.gensalt());
        member.setPassword(encPassword);
        memberRepository.save(member);
        return new ServiceResult(true);
    }

    /**
     * 회원정보 수정
     *
     * @param param
     */
    @Override
    public ServiceResult updateMember(MemberInput param) {
        String userId = param.getUserId();
        Optional<Member> optionalMember = memberRepository.findById(userId); // email
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        member.setPhone(param.getPhone());
        member.setUdtDt(LocalDateTime.now());
        memberRepository.save(member);
        return new ServiceResult(true);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username); // email

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if(Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())){
            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요");
        }

        if(Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())){
            throw new MemberStopUserException("정지된 회원 입니다.");
        }
//        if(!member.isEmailAuthYN()){
//            throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요");
//        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if(member.isAdminYn()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
