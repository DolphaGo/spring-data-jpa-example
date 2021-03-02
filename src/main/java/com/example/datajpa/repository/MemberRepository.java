package com.example.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
