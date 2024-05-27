package org.choongang.template;

import org.choongang.global.constants.Menu;
import org.choongang.template.main.MainTpl;

import java.util.HashMap;
import java.util.Map;

public class Templates {
    private static Templates instance;
    private Map<Menu, Template> tpls;

    private Templates() {
        tpls = new HashMap<>();
    }

    public static Templates getInstance() {
        if (instance == null) {
            instance = new Templates();
        }

        return instance;
    }

    public void render(Menu menu) {
        System.out.println(find(menu).getTpl());
    }

    public Template find(Menu menu) {
        Template tpl = tpls.get(menu);
        if (tpl != null) {
            return tpl;
        }

        switch (menu) {
            case JOIN:
            case LOGIN:
            case MYPAGE:
            default: tpl = new MainTpl();
        }

        tpls.put(menu, tpl);

        return tpl;
    }
}
