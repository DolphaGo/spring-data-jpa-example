package com.example.datajpa.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;

// Criteria는 너무 학문적입니다. 그냥 쓰지마세요;
public class MemberSpec {

    public static Specification<Member> teamName(final String teamName) {
        return (root, query, builder) -> {
            if (!StringUtils.hasLength(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인
            return builder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> userName(final String userName) {
        return (root, query, builder) -> builder.equal(root.get("username"), userName);
    }
}
