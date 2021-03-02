package com.example.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String username;

    protected Member() { //프록시에서 사용할 수 있으므로 private로는 막지 말자!
    }

    @Builder
    public Member(String username) {
        this.username = username;
    }

    // Setter보다 이게 더 낫다.
    public void changeUsername(String username) {
        this.username = username;
    }
}
