package com.example.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@EntityListeners(AuditingEntityListener.class) // 이게 귀찮다면 META-INF/orm.xml 을 만들어주면 된다.
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity { // 시간은 다 쓰지만, 등록자/수정자는 비즈니스에 따라 필요하지 않을 수 있다. 이렇게 구분하는 것을 추천한다.

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
