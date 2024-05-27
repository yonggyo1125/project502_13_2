package org.choongang.member.controllers;

import org.choongang.global.AbstractController;
import org.choongang.global.constants.Menu;
import org.choongang.main.MainRouter;
import org.choongang.template.Templates;

/**
 * 로그인 컨트롤러
 *
 */
public class LoginController extends AbstractController {

    @Override
    public void show() {

        Templates.getInstance().render(Menu.LOGIN);
    }

    @Override
    public void prompt() {
        String userId = promptWithValidation("아이디: ", s -> !s.isBlank());
        String userPw = promptWithValidation("비밀번호: ", s -> !s.isBlank());

        RequestLogin form = RequestLogin.builder()
                .userId(userId)
                .userPw(userPw)
                .build();

        System.out.println(form);
        // 로그인 처리 ...

        MainRouter.getInstance().change(Menu.MAIN);

    }
}
