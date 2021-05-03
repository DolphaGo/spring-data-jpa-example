package com.example.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.repository.MemberRepository;
import com.example.datajpa.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") // 트랜잭션이 없는 상황에서 조회가 되기 때문에 조회용으로만 사용해야 한다.
    public String findMember2(@PathVariable("id") Member member) { // PK를 data-jpa가 바로 조회해준다.
        return member.getUsername(); // 간단 간단할 때만 쓸 수 있다. 복잡하면 못쓴다.
    }

    /**
     * @implNote : 컨트롤러에서 파라미터가 바인딩 될 때, Pageable이 있으면 PageRequest를 생성해서 값을 채워줍니다. 스프링 자체가

    근데 디폴트 size가 20개야. 이런걸 좀 바꾸고 싶어
    1. 글로벌 설정(application.yml) -> spring.data.web.pageable.default-page-size : 10, max-page-size : 2000
    2. 어노테이션 직접 설정(글로벌 설정보다 우선함) -> @PageDefault(size = 5, sort = "username" ...)
     */
    // 사용예 : http://localhost:8080/members?page=4&size=3&sort=id,desc
    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 1000; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
