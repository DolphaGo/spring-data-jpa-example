package com.example.datajpa.repository;

import java.util.List;

import com.example.datajpa.entity.Member;

// Spring-Data-Jpa가 아니라 직접 구현한 것을 쓰고 싶을 때
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
