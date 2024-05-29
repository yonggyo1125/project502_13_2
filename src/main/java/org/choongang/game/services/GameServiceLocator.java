package org.choongang.game.services;

import org.choongang.game.constants.SubMenu;
import org.choongang.game.mapper.PointLogMapper;
import org.choongang.global.AbstractServiceLocator;
import org.choongang.global.Menu;
import org.choongang.global.Service;
import org.choongang.global.ServiceLocator;
import org.choongang.global.configs.DBConn;

public class GameServiceLocator extends AbstractServiceLocator {

    private static ServiceLocator instance;

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new GameServiceLocator();
        }

        return instance;
    }

    public PointLogMapper pointLogMapper() {
        return DBConn.getSession().getMapper(PointLogMapper.class);
    }

    @Override
    public Service find(Menu menu) {
        Service service = services.get(menu);
        if (service != null) {
            return service;
        }

        if (menu instanceof SubMenu) { // 혼자하기, 같이하기, 순위보기
            SubMenu subMenu = (SubMenu)menu;
            switch (subMenu) {
                case ALONE:
                case TOGETHER:
                case RANKING: service = new RankingInfoService(pointLogMapper()); break;
            }

        } else { // 주메뉴

        }

        services.put(menu, service);

        return service;
    }
}
