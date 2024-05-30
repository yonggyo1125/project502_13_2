package org.choongang.global.socket;

import java.time.LocalDateTime;

public class Ex02 {
    public static void main(String[] args) {
        Client c1 = new Client("user03", s -> {
            System.out.println(s);
        });

        while(true) {
            SocketData data = new SocketData("user03", "user02", "테스트", LocalDateTime.now());

            c1.send(data);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
    }
}
