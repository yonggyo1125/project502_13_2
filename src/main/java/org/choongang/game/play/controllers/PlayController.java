package org.choongang.game.play.controllers;

import org.choongang.game.constants.SubMenu;
import org.choongang.game.services.GamePlayService;
import org.choongang.game.services.GameServiceLocator;
import org.choongang.global.AbstractController;
import org.choongang.template.Templates;

public class PlayController extends AbstractController {

    @Override
    public void show() {
        String title = (SubMenu)menu == SubMenu.TOGETHER ? "플레이어를 선택하세요.\n":"컴퓨터와 대결!\n";

        Templates.getInstance().render(menu, () -> title);
    }

    @Override
    public void prompt() {
        GamePlayService service = (GamePlayService)GameServiceLocator.getInstance().find(menu);

        service.setScanner(sc);

        service.process(menu);
    }
}
