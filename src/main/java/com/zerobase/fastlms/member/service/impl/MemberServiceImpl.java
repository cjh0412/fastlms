package com.zerobase.fastlms.member.service.impl;

import com.zerobase.fastlms.compoents.MailCompoents;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.MemberInput;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailCompoents mailCompoents;

    /**
     *
     * 회원가입
     */
    @Override
    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember =
        memberRepository.findById(parameter.getUserId()); //id를 가지고 메서드가 존재하는지 확인
        if (optionalMember.isPresent()){// 동일 id 존재한다면
            return false;
        }
        String enPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(enPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYN(false)
                .emailAuthKey(uuid)
                .build();

//        Member member = new Member();
//        member.setUserId(parameter.getUserId());
//        member.setUserName(parameter.getUserName());
//        member.setPassword(parameter.getPassword());
//        member.setPhone(parameter.getPhone());
//        member.setRegDt(LocalDateTime.now());
//        member.setEmailAuthYN(false);
//        member.setEmailAuthKey(UUID.randomUUID().toString()); // 랜덤값 생성

        memberRepository.save(member);

        String email = parameter.getUserId();
        String subject = "fastlms  사이트 가입을 축하드립니다.";
        String text = "<p>fastlms  사이트 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>" +
                "<p><a href='http://localhost:8080/member/email-auth?id="+ uuid +"'>가입완료</a></p>";
        mailCompoents.sendMail(email, subject, text);

        return true;
    }

    @Override
    public boolean emailAuth(String uuid) {


        Optional<Member> optionalMember =
        memberRepository.findByEmailAuthKey(uuid);

        if(!optionalMember.isPresent()){
            return false;
        }
        Member member = optionalMember.get();
        member.setEmailAuthYN(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username); // email

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        log.info("=========User======");
        log.info("User : {}", optionalMember);

        return new User(member.getUserId(),  member.getPassword(),grantedAuthorities);
    }
}
