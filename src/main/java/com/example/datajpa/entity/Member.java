package com.example.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = { "id", "username", "age" }) // 가급적이면 연관관계 필드는 toString에 포함시키지 말 것.
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id") //DB테이블을 위한 명시적인 이름 짓기
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        changeTeam(team);
    }

    public void changeTeam(Team team) {
        if (this.team != null) {
            team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
    }

    // Setter보다 이게 더 낫다.
    public void changeUsername(String username) {
        this.username = username;
    }
}
