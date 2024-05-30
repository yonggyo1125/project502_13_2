package org.choongang.global.socket;

import java.time.LocalDateTime;

public class Ex01 {
    public static void main(String[] args) {
        Client c1 = new Client("user02", s -> {
            System.out.println(s);
        });

        while(true) {
            SocketData data = new SocketData("user02", "user03", "테스트", LocalDateTime.now());

            c1.send(data);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
    }
}
