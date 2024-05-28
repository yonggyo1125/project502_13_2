package org.choongang.member.mapper;

import org.choongang.member.controllers.SearchMember;
import org.choongang.member.entities.Member;

import java.util.List;

public interface MemberMapper {
    List<Member> getList(SearchMember search);
    Member get(String userId);
    Member get(long userNo);
    long getTotal(SearchMember search);
    int exist(String userId);

}
