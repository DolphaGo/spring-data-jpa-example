package com.example.datajpa.service;

import org.springframework.stereotype.Service;

import com.example.datajpa.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void test(){
        System.out.println(memberRepository.getClass()); // class : com.sun.proxy.$Proxy83 (프록시)
    }
}
