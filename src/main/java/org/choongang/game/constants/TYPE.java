package org.choongang.game.constants;

import java.util.Arrays;
import java.util.List;

public enum TYPE {
    SCISSOR("가위"),
    ROCK("바위"),
    PAPER("보");

    private final String title;

    TYPE(String title) {
        this.title = title;
    }

    public static List<String[]> getList() {
        return Arrays.asList(
                new String[] {SCISSOR.name(), SCISSOR.title()},
                new String[] {ROCK.name(), ROCK.title()},
                new String[] {PAPER.name(), PAPER.title()}
        );
    }

    public String title() {
        return title;
    }
}
