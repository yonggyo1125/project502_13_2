package org.choongang.member.services;

import org.choongang.global.Service;
import org.choongang.global.constants.MainMenu;
import org.choongang.member.constants.UserType;
import org.choongang.member.controllers.RequestJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JoinServiceTest {

    private Service service;
    private RequestJoin form;

    @BeforeEach
    void init() {
        form = RequestJoin.builder()
                .userId("u" + System.currentTimeMillis())
                .userPw("12345678")
                .confirmPw("12345678")
                .userNm("사용자")
                .userType(UserType.STUDENT.name())
                .build();
        service = MemberServiceLocator.getInstance().find(MainMenu.JOIN);
    }

    @Test
    @DisplayName("회원 가입시 예외 발생이 없고, 추가한 회원으로 조회가 되면 성공")
    void registerSuccessTest() {
        assertDoesNotThrow(() -> service.process(form));
    }
}
