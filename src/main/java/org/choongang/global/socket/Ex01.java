package org.choongang.global.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Ex01 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 9999);
        Scanner sc = new Scanner(System.in);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        Thread th1 = new Thread(() -> {
            while(true) {

                System.out.print("메세지: ");
                String message = sc.nextLine();
                try {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    SocketData data = SocketData
                            .builder()
                            .from("user01")
                            .to("all")
                            .message(message)
                            .build();
                    String json = om.writeValueAsString(data);
                    dos.writeUTF(json);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        th1.start();
    }
}
