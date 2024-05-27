package org.choongang.global;

import java.util.Scanner;

public abstract class AbstractController implements Controller {

    protected Scanner sc;

    public AbstractController() {
        sc = new Scanner(System.in);
    }

    /**
     * 상단 공통 출력 부분
     */
    public void common() {

    }

    /**
     * 입력 항목
     */
    public void prompt() {
        System.out.print("메뉴 선택: ");
        String menu = sc.nextLine();

    }

    @Override
    public final void run() {
        common();
        show();
        prompt();
    }
}
