package org.choongang.member.services;

import org.choongang.global.AbstractServiceLocator;
import org.choongang.global.Service;
import org.choongang.global.ServiceLocator;
import org.choongang.global.configs.DBConn;
import org.choongang.global.constants.Menu;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.validators.JoinValidator;

public class MemberServiceLocator extends AbstractServiceLocator {

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new MemberServiceLocator();
        }

        return instance;
    }

    // 회원가입 유효성 검사 Validator
    public JoinValidator joinValidator() {
        return new JoinValidator(memberMapper());
    }

    // MemberMapper 인터페이스 구현체
    public MemberMapper memberMapper() {
        return DBConn.getSession().getMapper(MemberMapper.class);
    }

    @Override
    public Service find(Menu menu) {
        Service service = services.get(menu);
        if (service != null) {
            return service;
        }

        switch (menu) {
            case JOIN: service = new JoinService(memberMapper(), joinValidator()); break;
            case LOGIN: service = new LoginService(); break;
        }

        return service;
    }
}
