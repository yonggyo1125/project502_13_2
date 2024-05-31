package org.choongang.member.services;

import org.choongang.global.Service;
import org.choongang.global.constants.MainMenu;
import org.choongang.member.MemberSession;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.controllers.RequestLogin;
import org.choongang.member.entities.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Service loginService;
    private Service joinService;
    private RequestLogin loginForm;
    private RequestJoin joinForm;

    @BeforeEach
    void init() {
        /**
         * 로그인 테스트는 회원 가입을 한 후 가입한 회원 정보로
         * 로그인이 되는지 테스트 한다. 즉, 가입먼저 시키고 테스트 진행
         */
        joinService = MemberServiceLocator.getInstance().find(MainMenu.JOIN);
        loginService = MemberServiceLocator.getInstance().find(MainMenu.LOGIN);

        joinForm = RequestJoin.builder()
                .userId("u" + System.currentTimeMillis())
                .userPw("12345678")
                .confirmPw("12345678")
                .userNm("사용자")
                .build();

        joinService.process(joinForm);

        loginForm = RequestLogin.builder()
                .userId(joinForm.getUserId())
                .userPw(joinForm.getUserPw())
                .build();
    }

    @Test
    @DisplayName("로그인 성공시 예외 발생 하지 않고, 로그인 상태 true, 회원정보 유지됨")
    void loginSuccessTest() {
        // 성공한다면 예외가 없어야 한다.
        assertDoesNotThrow(() -> loginService.process(loginForm));

        // 로그인 상태가 true
        assertTrue(MemberSession.isLogin());

        // 회원 정보가 있는지 체크
        Member member = MemberSession.getMember();
        assertEquals(loginForm.getUserId(), member.getUserId());
    }
}
