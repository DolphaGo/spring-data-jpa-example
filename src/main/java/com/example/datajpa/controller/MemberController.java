package com.example.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("DolphaGo"));
    }
}
