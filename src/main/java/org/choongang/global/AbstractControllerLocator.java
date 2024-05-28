package org.choongang.global;

import org.choongang.global.constants.MainMenu;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractControllerLocator implements ControllerLocator {

    protected static ControllerLocator instance;

    protected Map<MainMenu,Controller> controllers;

    protected  AbstractControllerLocator() {
        controllers = new HashMap<>();
    }
}
