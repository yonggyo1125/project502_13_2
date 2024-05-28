package org.choongang.game.controllers;

import org.choongang.game.constants.SubMenu;
import org.choongang.global.AbstractController;
import org.choongang.global.constants.MainMenu;
import org.choongang.main.MainRouter;
import org.choongang.template.Templates;

public class RankingController extends AbstractController {
    @Override
    public void show() {


        Templates.getInstance().render(SubMenu.RANKING, () -> {
            return "처리된 랭킹 목록\n";
        });

        // 랭킹 출력 후 게임 메뉴 이동
        MainRouter.getInstance().change(MainMenu.GAME);
    }

}
