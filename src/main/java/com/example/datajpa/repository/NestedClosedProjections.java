package com.example.datajpa.repository;

public interface NestedClosedProjections {

    String getUsername(); // username은 정확한 매칭으로 가져오지만

    TeamInfo getTeam(); // team은 전체 쿼리가 나간다. 즉 여기까지는 정확한 최적화가 안됨
    /**
     * 실제 나가는 쿼리는 다음과 같다.
     * select
     *         member0_.username as col_0_0_,
     *         team1_.team_id as col_1_0_,
     *         team1_.team_id as team_id1_2_,
     *         team1_.created_date as created_2_2_,
     *         team1_.updated_date as updated_3_2_,
     *         team1_.name as name4_2_
     *     from
     *         member member0_
     *     left outer join
     *         team team1_
     *             on member0_.team_id=team1_.team_id
     *     where
     *         member0_.username=?
     */

    //결론 : 엔티티 1개를 넘어가는 순간(조인이 들어가는 순간) 쓰기가 조금 애매해짐(최적화가 안돼서)
    interface TeamInfo {
        String getName();
    }
}
