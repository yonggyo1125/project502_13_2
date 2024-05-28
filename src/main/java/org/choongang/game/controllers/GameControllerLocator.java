package org.choongang.game.controllers;

import org.choongang.global.AbstractControllerLocator;
import org.choongang.global.Controller;
import org.choongang.global.ControllerLocator;
import org.choongang.global.Menu;

public class GameControllerLocator extends AbstractControllerLocator {

    private GameControllerLocator() {}

    public static ControllerLocator getInstance() {
        if (instance == null) {
            instance = new GameControllerLocator();
        }

        return instance;
    }

    @Override
    public Controller find(Menu menu) {

        Controller controller = controllers.get(menu);
        if (controller != null) {
            return controller;
        }



        return controller;
    }
}
