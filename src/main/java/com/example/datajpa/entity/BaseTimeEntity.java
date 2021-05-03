package com.example.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@EntityListeners(AuditingEntityListener.class) // 이게 귀찮다면 META-INF/orm.xml 을 만들어주면 된다.
@MappedSuperclass
public class BaseTimeEntity {
    @CreatedDate // spring-data 쪽 annotation
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
