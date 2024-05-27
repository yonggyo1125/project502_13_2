package org.choongang.template;

import org.choongang.global.constants.Menu;
import org.choongang.template.main.MainTpl;

public class Templates {
    public void render(Menu menu) {
        Template tpl = null;
        switch (menu) {
            case JOIN:
            case LOGIN:
            case MYPAGE:
            default: tpl = new MainTpl();
        }

        System.out.println(tpl.getTpl());
    }
}
