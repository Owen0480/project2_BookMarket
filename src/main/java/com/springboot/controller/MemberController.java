package com.springboot.controller;

import com.springboot.domain.Member;
import com.springboot.domain.MemberFormDto;
import com.springboot.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping(value = "/members")
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    // 신규 회원 등록 페이지 출력하기
    @GetMapping(value = "/add")
    public String requestAddMemberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/addMember";
    }

    // 신규 회원 등록하기
    @PostMapping(value = "/add")
    public String submitAddNewMember(@Valid MemberFormDto memberFormDto,
                                     BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "member/addMember";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/addMember";
        }
        return "redirect:/";
    }

    // 회원 정보 수정 페이지 출력하기
    @GetMapping(value = "/update/{memberId}")
    public String requestUpdateMemberForm(@PathVariable(name = "memberId") String memberId,
                                          Model model, Principal principal) {
        // 본인 확인
        if (principal != null && !principal.getName().equals(memberId)) {
            return "redirect:/";
        }

        Member member = memberService.getMemberById(memberId);

        // Member를 MemberFormDto로 변환
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setMemberId(member.getMemberId());
        memberFormDto.setName(member.getName());
        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setPhone(member.getPhone());
        memberFormDto.setAddress(member.getAddress());

        model.addAttribute("memberFormDto", memberFormDto);
        model.addAttribute("memberId", memberId);  // 이 줄 추가!

        return "member/updateMember";
    }

    // 회원 정보 수정하기
    @PostMapping(value = "/update")
    public String submitUpdateMember(@Valid MemberFormDto memberFormDto,
                                     BindingResult bindingResult, Model model,
                                     Principal principal) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("memberId", memberFormDto.getMemberId());  // 추가
            return "member/updateMember";
        }

        // 본인 확인
        if (principal != null && !principal.getName().equals(memberFormDto.getMemberId())) {
            return "redirect:/";
        }

        try {
            // 기존 회원 정보 가져오기
            Member existingMember = memberService.getMemberById(memberFormDto.getMemberId());

            // 정보 업데이트
            existingMember.setName(memberFormDto.getName());
            existingMember.setEmail(memberFormDto.getEmail());
            existingMember.setPhone(memberFormDto.getPhone());
            existingMember.setAddress(memberFormDto.getAddress());

            // 비밀번호가 입력되었으면 변경
            if (memberFormDto.getPassword() != null &&
                    !memberFormDto.getPassword().isEmpty()) {
                existingMember.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
            }

            memberService.saveMember(existingMember);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("memberId", memberFormDto.getMemberId());
            return "member/updateMember";
        }
        return "redirect:/";
    }

    // 회원 정보 삭제하기
    @GetMapping("/delete/{memberId}")
    public String deleteMember(@PathVariable(name = "memberId") String memberId,
                               Principal principal) {
        // 본인 확인
        if (principal != null && !principal.getName().equals(memberId)) {
            return "redirect:/";
        }

        memberService.deleteMember(memberId);
        return "redirect:/logout";
    }

    @GetMapping("/mypage")
    public String myPage(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        return "redirect:/members/update/" + principal.getName();
    }
    // 회원 가입 및 인증 시 인사말 페이지로 이동하기
    @GetMapping
    public String requestMain() {
        return "redirect:/";
    }
}