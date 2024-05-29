package org.choongang.game.mapper;

import org.apache.ibatis.session.SqlSession;
import org.choongang.global.configs.DBConn;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.entities.Member;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.services.JoinService;
import org.junit.jupiter.api.BeforeEach;

public class PointLogMapperTest {

    private MemberMapper memberMapper;
    private JoinService joinService;
    private PointLogMapper mapper;
    private Member member;

    @BeforeEach
    void init() {
        SqlSession session = DBConn.getSession();
        mapper = session.getMapper(PointLogMapper.class);
        memberMapper = session.getMapper(MemberMapper.class);

        RequestJoin form = RequestJoin.builder()
                .userId("u" + System.currentTimeMillis())
                .userPw("12345678")
                .confirmPw("12345678")
                .userNm("사용자")
                .build();
        joinService.process(form);

        member = memberMapper.get(form.getUserId());

    }
}
