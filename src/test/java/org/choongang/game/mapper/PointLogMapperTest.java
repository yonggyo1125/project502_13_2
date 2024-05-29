package org.choongang.game.mapper;

import org.apache.ibatis.session.SqlSession;
import org.choongang.game.entities.PointLog;
import org.choongang.global.configs.DBConn;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.services.JoinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PointLogMapperTest {

    private MemberMapper memberMapper;
    private JoinService joinService;
    private PointLogMapper mapper;
    private List<Member> members;

    @BeforeEach
    void init() {
        SqlSession session = DBConn.getSession();
        mapper = session.getMapper(PointLogMapper.class);
        memberMapper = session.getMapper(MemberMapper.class);
        members = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            RequestJoin form = RequestJoin.builder()
                    .userId("u" + System.currentTimeMillis())
                    .userPw("12345678")
                    .confirmPw("12345678")
                    .userNm("사용자")
                    .build();
            joinService.process(form);

            Member member = memberMapper.get(form.getUserId());
            members.add(member);
        }

    }

    @Test
    void mapperTest1() {
        for (Member member : members) {
            for (int i = 0; i < 50; i++) {
                int point = (int)(Math.random() * 3 - 1) * 10; // -10, 0, 10

                PointLog item = PointLog.builder()
                        .point(point)
                        .userNo(member.getUserNo())
                        .message("테스트 포인트")
                        .build();

                mapper.register(item);
            } // endfor
        } // endfor
    }
}
