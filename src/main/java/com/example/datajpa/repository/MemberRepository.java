package com.example.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy(); // 이건 전체 조회(By 뒤에 아무것도 없어서)

    //@Query(name = "Member.findByUsername")  // 없어도 된다!!
    //관례가 Member(엔티티명) 그다음 NamedQuery를 찾습니다. 그래서 Member.findByUsername에 선언된 namedQuery를 먼저 찾습니다.
    //그래서 없어도 동작합니다.
    //만약 NamedQuery도 없다면 method이름으로 Query를 생성합니다. 즉, 우선순위가 namedQuery -> Method 확인 -> Method이름으로 Query 생성
    //근데 NamedQuery는 실무에서 거의 사용하지 않습니다. 메서드에다가 쿼리를 바로 지정하는 방법이 훨씬 막강하다.
    ///하지만 NamedQuery가 em.createQuery와 다르게 가지는 장점이 하나 있습니다.
    // 바로 Application Loading 시점에 쿼리가 잘못됐다는 걸 미리 알려줍니다. 기본적으로 정적쿼리이기 때문에!!
    // 그래서 개발자는 어플리케이션 로딩시점에 파악할 수 있습니다.
    List<Member> findByUsername(@Param("username") String username);
}
