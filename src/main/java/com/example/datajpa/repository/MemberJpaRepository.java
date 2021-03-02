package com.example.datajpa.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.datajpa.entity.Member;

@Repository
public class MemberJpaRepository {
    @PersistenceContext // 이 어노테이션으로 스프링 컨테이너가 영속성 매니저를 가져옴
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
