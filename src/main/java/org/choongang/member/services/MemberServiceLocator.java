package org.choongang.member.services;

import org.choongang.global.AbstractServiceLocator;
import org.choongang.global.Service;
import org.choongang.global.ServiceLocator;
import org.choongang.global.constants.Menu;

public class MemberServiceLocator extends AbstractServiceLocator {

    @Override
    public Service find(Menu menu) {
        Service service = services.get(menu);
        if (service != null) {
            return service;
        }

        switch (menu) {
            case JOIN: service = new JoinService(); break;
            case LOGIN: service = new LoginService(); break;
        }

        return service;
    }
}
