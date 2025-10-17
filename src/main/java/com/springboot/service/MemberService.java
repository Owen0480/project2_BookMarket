package com.springboot.service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.springboot.domain.Member;
import com.springboot.repository.MemberRepository;
import jakarta.transaction.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public Member saveMember(Member member) { // 회원 정보 저장하기
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }
    public Member getMemberById(String memberId) { // 회원 정보 가져오기
        Member member = memberRepository.findByMemberId(memberId);
        return member;
    }
    public void deleteMember(String memberId) { // 회원 삭제하기
        Member member = memberRepository.findByMemberId(memberId);
        memberRepository.deleteById(member.getNum());
    }
    private void validateDuplicateMember(Member member) { // 회원 id 중복 체크하기
        Member findMember = memberRepository.findByMemberId(member.getMemberId());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    // 인증 시 회원 정보 가져오기
    @Override
    public UserDetails loadUserByUsername(String id) throws
            UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(id);
        if(member == null) {
            throw new UsernameNotFoundException(id);
        }
        return User.builder()
                .username(member.getMemberId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}