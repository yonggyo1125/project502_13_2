package org.choongang.game.services;

import org.choongang.game.constants.SubMenu;
import org.choongang.global.Menu;
import org.choongang.global.Service;
import org.choongang.global.socket.Client;
import org.choongang.global.socket.SocketData;
import org.choongang.member.MemberSession;
import org.choongang.member.entities.Member;

import java.util.Scanner;

public class GamePlayService implements Service<Menu> {

    private Scanner sc;

    private String player;

    public void setScanner(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public void process(Menu menu) {
        SubMenu subMenu = (SubMenu)menu;
        if (subMenu == SubMenu.TOGETHER) {
            playTogether();
        } else {
            playAlone();
        }
    }

    // 같이 하기
    public void playTogether() {
        Member member = MemberSession.getMember();
        String name = member.getUserId();
        Client player = new Client(name, this::dataProcess);

        // 플레이어 선택을 위한 사용자 목록 요청
        player.send(new SocketData("request_users"));
    }

    // 혼자 하기
    public void playAlone() {

    }

    // 소켓 데이터 처리
    public void dataProcess(SocketData data) {
        String to = data.getTo();
        if (to.equals("request_users")) { // 사용자 선택
            selectPlayUser(data.getMessage());
        }
    }

    private void selectPlayUser(String message) {
        Member member = MemberSession.getMember();
        String userId = member.getUserId();
        if (message == null || message.replace(userId, "").isBlank()) {
            System.err.println("플레이할 사용자가 없습니다.");
        }



        System.out.println("플레이어 목록");
        System.out.println(message);
        /*
        Arrays.stream(players).filter(s -> !s.equals(userId))
                  .forEach(System.out::println);
          */
        System.out.print("플레이어 아이디: ");
        player = sc.nextLine();
    }
}
