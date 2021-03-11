package com.example.datajpa.repository;

import java.util.List;
import java.util.Optional;

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

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                 .setParameter("username", username)
                 .setParameter("age", age)
                 .getResultList();
    }

    //NamedQuery는 실무에서 거의 쓰지 않는다.
    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                 .setParameter("username", username)
                 .getResultList();
    }

    //pagingQuery
    // DB가 바뀌면 어떻게 하나요? 문제 없나요?
    // 문제 없습니다. jpql은 기본적으로 방언에 기반하여 쿼리가 날아가기 때문에 데이터베이스에 맞게 쿼리가 날아갑니다.
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                 .setParameter("age", age)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                 .setParameter("age", age)
                 .getSingleResult();
    }
}
