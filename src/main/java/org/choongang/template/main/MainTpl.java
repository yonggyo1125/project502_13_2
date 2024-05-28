package org.choongang.template.main;

import org.choongang.member.MemberSession;
import org.choongang.member.entities.Member;
import org.choongang.template.Template;

public class MainTpl implements Template {
    @Override
    public String getTpl() {
        StringBuffer sb = new StringBuffer(2000);

        sb.append("메뉴를 선택하세요.\n");
        if (MemberSession.isLogin()) {
            Member member = MemberSession.getMember();
            sb.append(String.format("%s(%s)님 로그인\n", member.getUserNm(), member.getUserId()));
            sb.append("1. 학생관리\n");
            sb.append("2. 출결관리\n");
            sb.append("3. 성적관리\n");
            sb.append("4. 로그아웃\n");
        } else {
            sb.append("1. 회원가입\n");
            sb.append("2. 로그인\n");
        }
        sb.append("--------------------------------\n");

        return sb.toString();
    }
}
