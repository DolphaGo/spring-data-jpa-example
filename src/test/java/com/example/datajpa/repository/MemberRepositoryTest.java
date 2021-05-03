package com.example.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @DisplayName("Member 테스트")
    @Test
    public void testMember() throws Exception {
        Member member = Member.builder().username("memberB").build();
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 같은 트랜잭션에서는 영속성 컨텍스트의 동일성 보장!
    }

    @DisplayName("Member 기본 CRUD 체크")
    @Test
    public void basicCRUD() throws Exception {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검사
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("member1!!!!!!!!!!!!!");
//
//        //리스트 조회 검증
//        List<Member> all = memberJpaRepository.findAll();
//        assertThat(all.size()).isEqualTo(2);
//
//        //카운트 검
//        long count = memberJpaRepository.count();
//        assertThat(count).isEqualTo(2);
//
//        //삭제 검증
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
    }

    @DisplayName("findByUsernameAndAgeGreaterThen 테스트")
    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("Method Query Test")
    @Test
    public void find_hello_by() throws Exception {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @DisplayName("NamedQuery 테스트")
    @Test
    public void named_query() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findByUsername("AAA");

        assertEquals(10, aaa.get(0).getAge());
    }

    @DisplayName("레포지토리에 쿼리 정의 테스트")
    @Test
    public void query_test() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertEquals(m1, result.get(0));
    }

    @DisplayName("findUsernameList 테스트")
    @Test
    public void find_username_list_query_test() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @DisplayName("JPQL로 Dto로 받아오는 것 테스트")
    @Test
    public void findMemberDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("aaa", 10);
        member.setTeam(team);
        memberRepository.save(member);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        for (MemberDto dto : memberDtos) {
            System.out.println("dto = " + dto);
        }
    }

    @DisplayName("SQL in절 테스트, 리스트 객체도 파라미터 바인딩이 된다!")
    @Test
    public void findByNames() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @DisplayName("다양한 리턴타입 테스트")
    @Test
    public void retrunType_Test() throws Exception {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        System.out.println("List ==>" + aaa.get(0).getUsername());
        Member aaa1 = memberRepository.findOneMemberByUsername("AAA");
        System.out.println("그냥 Member ==>" + aaa1.getUsername());
        Optional<Member> aaa2 = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("Optional Member ==>" + aaa2.get().getUsername());

        //없는 것을 찾으려고 할 때
        List<Member> asdasdasd = memberRepository.findListByUsername("asdasd");
        System.out.println("result = " + asdasdasd.size()); //없으면 empty Collection이 조회가 된다.

        Member asdasdasd1 = memberRepository.findOneMemberByUsername("asdasd");
        System.out.println("asdasdasd1 = " + asdasdasd1); // null이 출력된다.
        // jpa는 없으면 NoResultException이 터지는데, spring-data-jpa는 이걸 try~catch로 감싸가지고 null로 반환합니다. (이게 jpa랑 다른 점)
        // 근데 이건 논쟁이 꽤 있었는데, 이제 자바8이 생기면서 Optional이 생겨나면서 이에 대한 논쟁이 사라짐

        Optional<Member> asdasdasd2 = memberRepository.findOptionalMemberByUsername("asdasdasd");
        System.out.println("asdasdasd2 = " + asdasdasd2); // Optional.empty
        // 데이터 조회했을 때 있을 수도, 없을 수도 있으니까 이럴 땐 Optional 쓰는 것이 맞습니다.

        /*
        근데, 2개가 있는데 하나만 조회할 땐 NonUniqueResultException이 터진다.
         */
        Member m3 = new Member("CCC", 10);
        Member m4 = new Member("CCC", 20);
        memberRepository.save(m3);
        memberRepository.save(m4);

        // NonUniqueResultException이 터지는데, spring-data-jpa가 이걸 spring의 Exception 이름인 IncorrectResultSizeDataAccessException로 변환한다!
        // 스프링 추상화이기 때문에 다른 jpa 구현체를 써도, 이걸 사용하는 클라이언트 코드 쪽에서 예외를 또 고칠 필요가 없습니다.
        // 그래서 이렇게 Exception을 한 번 변환해서 리턴해주고 있습니다.
//        Member ccc = memberRepository.findOneMemberByUsername("CCC"); // NonUniqueResultException -> IncorrectResultSizeDataAccessException

//        메서드 이름을 이상하게 했을 때
        Optional<Member> aaaaaaaaaaaaaaa = memberRepository.findAaaaaaByUsername("AAA");
        aaaaaaaaaaaaaaa.ifPresent(System.out::println);
    }

    @DisplayName("NamedQueryTest")
    @Test
    public void named_query_test() throws Exception {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findByUsername("AAA");

        assertEquals(10, aaa.get(0).getAge());

    }

    /**
     * @implNote : 페이징은 딱 잘라서 가져오기에 최적화하기가 쉬운데 totalCount는 크기가 커지면 견적이 너무 커진다.
     * 성능이 너무 저하되는 문제가 있는데(조인을 겁나 해서) 조인을 하지 않아도 되는 케이스가 많다.
     * 그래서 countQuery를 분리하는 작업을 할 수 있습니다.
     */
    @DisplayName("페이징 테스트")
    @Test
    public void paging() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // spring-data-jpa는 page를 0부터 시작합니다! 1이 아니에요. 주의하세요!!
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
        // 0페이지에서 3개가져와, 그리고 옵션으로 소팅도 하고 싶으면 소팅도 넣을 수 있다!

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); // 내부적으로 totalCount계산도 해주고 있다.
        // select count(member0_.member_id) as col_0_0_ from member member0_ where member0_.age=10;

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("total = " + totalElements);
        // totalQuery를 날린 적도 없는데, totalQuery가 날라간다.

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @DisplayName("페이징 결과는 항상 DTO로 변환하여 클라이언트에 반환해야 한다.")
    @Test
    public void paging_to_dto() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> mapPage = page.map(member -> new MemberDto(member.getId(), member.getUsername(), "TeamName"));

        mapPage.forEach(m -> System.out.println("=================> " + m));
    }

    @DisplayName("슬라이스 테스트")
    @Test
    public void slice() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // spring-data-jpa는 page를 0부터 시작합니다! 1이 아니에요. 주의하세요!!
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
        // 0페이지에서 3개가져와, 그리고 옵션으로 소팅도 하고 싶으면 소팅도 넣을 수 있다!

        Slice<Member> page = memberRepository.findByAgeSlice(age, pageRequest);
        // 쿼리 결과 : select member0_.member_id as member_i1_0_, member0_.age as age2_0_, member0_.team_id as team_id4_0_, member0_.username as username3_0_ from member member0_ left outer join team team1_ on member0_.team_id=team1_.team_id order by member0_.username desc limit 4;
        // 3개를 요청했지만, 1개 더(limit 4) 여유롭게 가져오는 것을 확인할 수 있다. 다음페이지가 있다면 [더보기]등을 지원하기 위함
        // 그리고 TotalCount를 가져오지 않습니다.

        List<Member> content = page.getContent();
        //slice는 토탈 개수를 몰라요. 알 필요가 없으니까 (더보기 버튼 눌러서 페이지 더 불러오고...)
        System.out.println("content.size() = " + content.size());

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @DisplayName("단순하게 앞에거 3개만 가져올래")
    @Test
    public void getTop3() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        // 페이지 쿼리 안쓰고 그냥 Top3를 해줘도 됩니다.
        List<Member> list = memberRepository.findTop3ByAgeOrderByUsernameDesc(age);

        for (Member member : list) {
            System.out.println("member = " + member);
        }
    }

    @DisplayName("Entity는 절대로 api쪽으로 바로 내보내서는 안됩니다.")
    @Test
    public void mapToDto() throws Exception {
        Team team = new Team("teamA");
        teamRepository.save(team);
        Member member = new Member("member1", 10);
        member.setTeam(team);
        memberRepository.save(member);
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 다음과 같이 하면 page를 유지하면서 Dto로 반환할 수 있습니다.
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), team.getName()));
    }

    @DisplayName("벌크 업데이트")
    @Test
    void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkUpdatePlus(20); // DB에는 member5가 41살로 업데이트가 되었을 것임
        assertEquals(3, resultCount);

        List<Member> member5 = memberRepository.findByUsername("member5"); // 이 결과가 41살일까, 40살일까?
        System.out.println("member5 = " + member5.get(0));
        assertEquals(41, member5.get(0).getAge()); // 그냥 하면 40 살이다. Why? 이게 벌크 연산에서 조심해야 한다. (영속 컨텍스트를 비워줘야한다.)
        // save를 했을 때의 영속성 컨텍스트에 있는 40살을 가져오기 때문이다. (영속 컨텍스트에는 반영이 안됨)
        // 40살인 쿼리가 있는데, 이거 영속 컨텍스트안에 있는 값을 가져와서
        // 벌크 연산을 해서 DB에는 41, 영속 컨텍스트는 40이라서 40으로 출력이 되는 것이다.

        /**
         * jpa는 영속성 컨텍스트라는 개념이 있어서,
         * 벌크 연산은 영속성 컨텍스트를 모르는 상태로 쿼리를 날려버림.
         * 따라서 벌크 연산을 하고 나서는 영속성 컨텍스트를 다 날려야 합니다!!
         * 어떻게요?
         */
    }

    @DisplayName("영속성 컨텍스트에 있는 것을 조회할 때 또 쿼리가 나갈까?")
    @Test
    void my_query_test() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        List<Member> member11 = memberRepository.findByUsername("member1"); // PK로 찾으면 안나가고, 그 외는 나감. 그런다음 영속 컨텍스트와 비교함
        System.out.println("member11 = " + member11);
    }

    @DisplayName("N+1문제")
    @Nested
    class N_Plus_one_problem {

        @DisplayName("findAll 오버라이드해서 페치조인하기 전엔 N+1 문제가 있다.")
        @Test
        void n_plus_one_problem() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamB);

            memberRepository.save(member1);
            memberRepository.save(member2);

            em.flush();
            em.clear();

            // N+1 문제
            List<Member> members = memberRepository.findAll();
            for (Member member : members) {
                System.out.println("member = " + member);
                System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // 프록시 초기화
            }
        }

        @DisplayName("findAll 오버라이드해서 페치조인 해버리기(페치조인은 EntityGraph로 구현)")
        @Test
        void find_all_override() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamB);

            memberRepository.save(member1);
            memberRepository.save(member2);

            em.flush();
            em.clear();

            // EntityGraph로 페치조인을 구현해놨기에 N+1 문제가 없다
            List<Member> members = memberRepository.findAll();
            for (Member member : members) {
                System.out.println("member = " + member);
                System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시가 아니다.
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }
        }

        @DisplayName("JPQL + FetchJoin(EntityGraph)")
        @Test
        void jpql_and_entity_graph() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamB);

            memberRepository.save(member1);
            memberRepository.save(member2);

            em.flush();
            em.clear();

            // EntityGraph로 페치조인을 구현해놨기에 N+1 문제가 없다 (jpql + entityGraph)
            List<Member> members = memberRepository.findMemberByUsername();
            for (Member member : members) {
                System.out.println("member = " + member);
                System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시가 아니다.
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }
        }

        @DisplayName("method 쿼리에 entitygraph 붙이기")
        @Test
        void method_query_plus_entitygraph() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamB);

            memberRepository.save(member1);
            memberRepository.save(member2);

            em.flush();
            em.clear();

            // EntityGraph로 페치조인을 구현해놨기에 N+1 문제가 없다
            List<Member> members = memberRepository.findEntityGraphByUsername("member1");
            for (Member member : members) {
                System.out.println("member = " + member);
                System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시가 아니다.
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }
        }

        @DisplayName("NamedEntityGraph 사용")
        @Test
        void named_entitygraph() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");

            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamB);

            memberRepository.save(member1);
            memberRepository.save(member2);

            em.flush();
            em.clear();

            // EntityGraph로 페치조인을 구현해놨기에 N+1 문제가 없다
            List<Member> members = memberRepository.findNamedEntityGraphByUsername("member1");
            for (Member member : members) {
                System.out.println("member = " + member);
                System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass()); // 프록시가 아니다.
                System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            }
        }
    }

    @DisplayName("queryHint Test")
    @Nested
    class QueryHint {
        @DisplayName("변경 감지")
        @Test
        void queryHint() {
            Member member = new Member("member1", 10);
            memberRepository.save(member);
            em.flush();
            em.clear();

            Member findMember = memberRepository.findById(member.getId()).get();
            findMember.setUsername("member2");
            em.flush();// 변경감지로 인해 업데이트 쿼리가 나감
            /**
             *     update
             *         member
             *     set
             *         age=?,
             *         team_id=?,
             *         username=?
             *     where
             *         member_id=?
             */
        }

        @DisplayName("변경 감지 필요 없어, 조회용으로만 쓸거야")
        @Test
        void queryHint2() {
            // 나 100% 조회용으로만 쓸거야, 진짜
            Member member = new Member("member1", 10);
            memberRepository.save(member);
            em.flush();
            em.clear();

            // Jpa 쿼리 힌트로 ReadOnly 속성을 줬기에 내부적으로 스냅샷을 뜨지 않음
            Member findMember = memberRepository.findReadOnlyByUsername(member.getUsername());
            findMember.setUsername("member2");
            em.flush();// 내부적으로 스냅샷을 뜨지 않아서 업데이트 쿼리가 안 나감
        }
    }

    @DisplayName("비관적 락")
    @Test
    void pessimist_lock() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // select for update~
        List<Member> lockByUsername = memberRepository.findLockByUsername(member.getUsername());
        em.flush();
        /** 쿼리 결과
         *     select
         *         member0_.member_id as member_i1_0_,
         *         member0_.age as age2_0_,
         *         member0_.team_id as team_id4_0_,
         *         member0_.username as username3_0_
         *     from
         *         member member0_
         *     where
         *         member0_.username=? for update
         */
        // 실시간 트래픽이 많은 서비스에서는 비관락 사용 ㄴㄴ, 돈과 같이 엄청 중요한 것 아닌 이상.
        // 실시간 서비스에서는 락을 건다면 낙관락으로 풀어가는 방법을 추천한다.
    }


    @DisplayName("callCustom Repository")
    @Test
    void callCustom(){ // querydsl을 쓸 때 커스텀 방식을 많이 사용합니다.
        // 간단한 것은 DataJpa를 쓰는데, 복잡한 것은 querydsl을 사용해서, 이런 커스텀 방식을 많이 사용해요
        // 항상 사용자 정의 리포지토리가 필요한 것은 아님
        // 그러나, 핵심 레포지토리에서 사용해야 하는 쿼리 / DTO, 통계 등 쿼리, 복잡한 쿼리 등 는 구분합니다.
        // 영한님은 핵심 비즈니스 로직 / 특정 목적용 복잡한 쿼리 클래스를 아예 구분해서 사용하는 쿼리를 구분한다고 하십니다.
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }
}
