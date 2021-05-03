package com.example.datajpa.dto;

import com.example.datajpa.entity.Member;

import lombok.Data;

// 의존 관계가 엔티티는 DTO를 보지 못하게, DTO는 엔티티를 봐도 된다.
@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
