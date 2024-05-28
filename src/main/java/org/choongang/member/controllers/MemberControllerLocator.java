package org.choongang.member.controllers;

import org.choongang.global.AbstractControllerLocator;
import org.choongang.global.Controller;
import org.choongang.global.ControllerLocator;
import org.choongang.global.constants.MainMenu;

public class MemberControllerLocator extends AbstractControllerLocator {

    private MemberControllerLocator() {}

    public static ControllerLocator getInstance() {
        if (instance == null) {
            instance = new MemberControllerLocator();
        }

        return instance;
    }


    @Override
    public Controller find(MainMenu mainMenu) {
        Controller controller = controllers.get(mainMenu);
        if (controller != null) {
            return controller;
        }

        switch(mainMenu) {
            case JOIN: controller = new JoinController(); break;
            default: controller = new LoginController();
        }

        controllers.put(mainMenu, controller);

        return controller;
    }
}
