package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.Service;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.validators.JoinValidator;

@RequiredArgsConstructor
public class JoinService implements Service<RequestJoin> {

    private final MemberMapper mapper;
    private final JoinValidator validator;

    @Override
    public void process(RequestJoin form) {

    }
}
