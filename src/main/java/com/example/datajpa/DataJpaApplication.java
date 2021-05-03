package com.example.datajpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.datajpa.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

//@EnableJpaRepositories(basePackages = "com.example.datajpa.repository") // 원래는 이렇게 해줘야하는데 SpringBoot는 이런 세팅 안해줘도 됩니다.
@EnableJpaAuditing // Data-Jpa용 Auditing
@SpringBootApplication
@RequiredArgsConstructor
public class DataJpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() { // 등록되거나 수정될 때마다 결과물을 꺼내서 createBy, lastModifiedBy
        // 에 값을 채워줍니다.
        // 여기서 SpringSecurityContext에서 세션정보를 가져와서 꺼내거나 해서, 유저 아이디를 넣어주면 됩니다. 지금은 예시로 UUID 랜덤 넣음
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
