package com.example.datajpa.service;

import org.springframework.stereotype.Service;

import com.example.datajpa.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void test(){
        log.info("-------Repository class------- : {}",memberRepository.getClass()); // class : com.sun.proxy.$Proxy83 (프록시)
    }
}
