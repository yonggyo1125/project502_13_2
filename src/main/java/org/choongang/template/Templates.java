package org.choongang.template;

import org.choongang.global.constants.MainMenu;
import org.choongang.template.main.MainTpl;
import org.choongang.template.member.JoinTpl;
import org.choongang.template.member.LoginTpl;

import java.util.HashMap;
import java.util.Map;

public class Templates {
    private static Templates instance;
    private Map<MainMenu, Template> tpls;

    private Templates() {
        tpls = new HashMap<>();
    }

    public static Templates getInstance() {
        if (instance == null) {
            instance = new Templates();
        }

        return instance;
    }

    public void render(MainMenu mainMenu) {

        System.out.println(find(mainMenu).getTpl());
    }

    public Template find(MainMenu mainMenu) {
        Template tpl = tpls.get(mainMenu);
        if (tpl != null) {
            return tpl;
        }

        switch (mainMenu) {
            case JOIN: tpl = new JoinTpl(); break;
            case LOGIN: tpl = new LoginTpl(); break;
            default: tpl = new MainTpl();
        }

        tpls.put(mainMenu, tpl);

        return tpl;
    }

    public String line() {
        return "-----------------------------------\n";
    }

    public String doubleLine() {
        return "===================================\n";
    }
}
