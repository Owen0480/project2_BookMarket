package com.springboot;

import com.springboot.domain.Member;
import com.springboot.domain.Role;
import com.springboot.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookMarketApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookMarketApplication.class, args);
	}

	// 관리자 정보를 Member 엔티티에 등록
	@Bean
	public CommandLineRunner run(MemberService memberService) throws Exception {
		return (String[] args) -> {
			// 이미 Admin이 있으면 등록하지 않음
			try {
				Member member = new Member();
				member.setMemberId("Admin");
				member.setName("관리자");
				member.setPhone("");
				member.setEmail("");
				member.setAddress("");
				String password = new BCryptPasswordEncoder().encode("Admin1234");
				member.setPassword(password);
				member.setRole(Role.ADMIN);
				memberService.saveMember(member);
				System.out.println("관리자 계정이 생성되었습니다.");
			} catch (IllegalStateException e) {
				System.out.println("관리자 계정이 이미 존재합니다.");
			}
		};
	}
}