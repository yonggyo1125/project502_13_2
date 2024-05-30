package org.choongang.template.game;

import org.choongang.template.Template;
import org.choongang.template.Templates;

import java.util.function.Supplier;

public class PlayTpl implements Template {

    private Supplier<String> hook;

    @Override
    public String getTpl() {
        StringBuffer sb = new StringBuffer();
        sb.append("묵찌빠 게임~!\n");
        sb.append(Templates.getInstance().line());
        sb.append(hook.get());

        return "";
    }

    @Override
    public void addHook(Supplier<String> hook) {
        this.hook = hook;
    }
}
