package org.choongang.member.controllers;

import org.choongang.global.AbstractController;
import org.choongang.global.constants.Menu;
import org.choongang.template.Templates;

import java.util.function.Predicate;

/**
 * 회원 가입 컨트롤러
 *
 */
public class JoinController extends AbstractController {
    @Override
    public void show() {
        Templates.getInstance().render(Menu.JOIN);
    }

    @Override
    public void prompt() {
        String userId = promptWithValidation("아이디(6자리 이상): ", s -> s.length() >= 6);

        String userPw = promptWithValidation("비밀번호(8자리 이상): ", s -> s.length() >= 8);

        String confirmPw = promptWithValidation("비밀번호 확인: ", s -> {
           boolean match = s.equals(userPw);
            System.err.println("\n비밀번호가 일치하지 않습니다.");
            System.out.println("비밀번호 확인: ");
           return match;
        });

        String userNm = promptWithValidation("회원명: ", s -> !s.isBlank());

        System.out.printf("userId=%s, userPw=%s, confirmPw=%s, userNm=%s%n", userId, userPw, confirmPw, userNm);
    }
}
