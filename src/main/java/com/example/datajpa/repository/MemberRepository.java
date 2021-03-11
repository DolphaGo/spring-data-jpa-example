package com.example.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.datajpa.dto.MemberDto;
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

    //이게 왜 좋냐면, 어플리케이션 로딩시점에, 쿼리 오류를 잡아줍니다. 모르는 property인데..?
    //여기서 정의한 것은 그냥 이름이 없는 NamedQuery라고 보면 됩니다. 애플리케이션 로딩시점에 미리 쿼리를 파싱해서 분석합니다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //Dto로 조회할때는 new 생성자로 적는 것처럼 패키지명까지 모두 적어줘야 한다.
    //이게 jpql이 제공하는 문법이고, 이렇게하면 Dto로 받을 수 있습니다.
    @Query("select new com.example.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /**
     *  JPA는 다양한 반환 타입을 지원한다!!
     */

    List<Member> findListByUsername(String username); // 컬렉션
    Member findOneMemberByUsername(String username); // 단건
    Optional<Member> findOptionalMemberByUsername(String username); // 단건 Optional

    Optional<Member> findAaaaaaByUsername(String username); // 단건 Optional

}
