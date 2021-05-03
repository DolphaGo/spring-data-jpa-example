package com.example.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.datajpa.entity.Member;

import lombok.RequiredArgsConstructor;

// 실무를 하면서 CQRS, 핵심/비핵심 쿼리 구분, LifeCycle에 따라 변경사항 등을 고민하면서 클래스를 쪼개는 과정이 필요해질 것이다.
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
