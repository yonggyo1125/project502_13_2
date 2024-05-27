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
        System.out.print("아이디: ");
        String userId = sc.nextLine();
        System.out.println(userId);
    }


}
