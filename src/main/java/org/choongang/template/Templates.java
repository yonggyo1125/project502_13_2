package org.choongang.template;

import org.choongang.global.constants.Menu;
import org.choongang.template.main.MainTpl;

public class Templates {
    private static Templates instance;
    private Templates() {}
    public static Templates getInstance() {
        if (instance == null) {
            instance = new Templates();
        }

        return instance;
    }

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
