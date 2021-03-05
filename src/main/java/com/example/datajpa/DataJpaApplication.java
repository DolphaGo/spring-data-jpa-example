package com.example.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.datajpa.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

//@EnableJpaRepositories(basePackages = "com.example.datajpa.repository") // 원래는 이렇게 해줘야하는데 SpringBoot는 이런 세팅 안해줘도 됩니다.
@SpringBootApplication
@RequiredArgsConstructor
public class DataJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

}
