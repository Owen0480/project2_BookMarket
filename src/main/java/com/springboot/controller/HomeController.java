package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import com.springboot.domain.Member;
import com.springboot.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @Autowired
    private MemberService memberService;

    // 웹 요청 URL이 /인 경우에 호출하는 메서드
    @RequestMapping("/")
    public String welcome(Model model, Authentication authentication, HttpServletRequest
            httpServletRequest) {
        if (authentication == null) // 인증 전 처리
            return "welcome";
// 인증 후 처리
        User user = (User) authentication.getPrincipal();
        String userId = user.getUsername();
        if (userId == null)
            return "redirect:/login";
        Member member = memberService.getMemberById(userId);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute("userLoginInfo", member); // 사용자 정보의 세션 등록
        return "welcome";
    }
}
