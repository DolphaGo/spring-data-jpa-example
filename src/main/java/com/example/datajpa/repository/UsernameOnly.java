package com.example.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    @Value("#{target.username + ' ' + target.age}") // Open Projection : username과 age를 가져와서 이어 붙이는 것
    String getUsername(); // 인터페이스 기반의 Close Projection이라고 한다. 정확한 매칭을 말함.
    /**
     * 왜 Open Projection이라고 하냐?
     *     select
     *         member0_.member_id as member_i1_1_,
     *         member0_.created_date as created_2_1_,
     *         member0_.last_modified_date as last_mod3_1_,
     *         member0_.created_by as created_4_1_,
     *         member0_.last_modified_by as last_mod5_1_,
     *         member0_.age as age6_1_,
     *         member0_.team_id as team_id8_1_,
     *         member0_.username as username7_1_
     *     from
     *         member member0_
     *     where
     *         member0_.username=?
     *   우선 Member 엔티티를 다 가져온다음에, 결과가 나온 것에서 원하는 데이터를 찍어서 가져오는 방식이다.
     *   그리고, UsernameOnly.getUsername()을 하면 다음과 같은 결과가 나온다(example)
     *   usernameOnly = member1 0
     *   usernameOnly = member1 2
     */
}
