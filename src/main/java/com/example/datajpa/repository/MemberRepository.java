package com.example.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.NamedEntityGraph;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    // 컬렉션 파라미터 바인딩!! in 절로 들어간다.
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /**
     *  JPA는 다양한 반환 타입을 지원한다!!
     */

    List<Member> findListByUsername(String username); // 컬렉션

    Member findOneMemberByUsername(String username); // 단건

    Optional<Member> findOptionalMemberByUsername(String username); // 단건 Optional

    Optional<Member> findAaaaaaByUsername(String username); // 단건 Optional

//    // paging (인터페이스만 있어도, 구현을 하지 않았음에도 페이징이 가능하다.)
//    Page<Member> findByAge(int age, Pageable pageable); //pageable은 페이지에 대한 조건(여긴 1페이지야, 2페이지야..)

//    Slice<Member> findByAge(int age, Pageable pageable);

    /**
     * @implNote : 그냥 카운트 쿼리는 join을 하고 있다.
     *     select
     *         count(member0_.member_id) as col_0_0_
     *     from
     *         member member0_
     *     left outer join
     *         team team1_
     *             on member0_.team_id=team1_.team_id
     */
//    @Query(value = "select m from Member m left join m.team t")
//    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * @implNote : 여기서는 굳이 Join이 필요하지가 않는 Count이다.
     * 따라서 카운트 쿼리를 분리해서 Member의 개수만 세준다.
     * where절로 데이터를 필터링하지 않는다는 조건하에, left outer join이라면, 굳이 조인절을 섞을 필요가 없다.
    그런 상황에서는 자동으로 짜주는 쿼리 대신에 count Query를 직접 좀 더 최적화 할 수 있다.
    select count(member0_.member_id) as col_0_0_ from member member0_
     */
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t")
    Slice<Member> findByAgeSlice(int age, Pageable pageable);

    List<Member> findTop3ByAgeOrderByUsernameDesc(int age);

    // 벌크 업데이트 , clearAutomatically 옵션을 넣어주면, 벌크 업데이트 후 영속 컨텍스트를 비워주는 작업을 자동으로 해준다.
    @Modifying(clearAutomatically = true) // 이게 꼭 필요함 (executeUpdate 처럼, 벌크성 업데이트를 위함)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkUpdatePlus(@Param("age") int age);

    @Override
    @EntityGraph(attributePaths = "team")
    List<Member> findAll();

    @EntityGraph(attributePaths = { "team" })
    @Query("select m from Member m")
    List<Member> findMemberByUsername();

    @EntityGraph(attributePaths = { "team" })
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);
}
