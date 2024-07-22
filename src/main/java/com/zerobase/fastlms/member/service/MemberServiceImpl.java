package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

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

        Member member = new Member();
        member.setUserId(parameter.getUserId());
        member.setUserName(parameter.getUserName());
        member.setPassword(parameter.getPassword());
        member.setPhone(parameter.getPhone());
        member.setRegDt(LocalDateTime.now());

        memberRepository.save(member);

        return true;
    }
}
