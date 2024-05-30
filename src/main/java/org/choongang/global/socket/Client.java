package org.choongang.global.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private String name;
    private ObjectMapper om;

    public Client(String name, Consumer<SocketData> handler) {
        this.name = name;
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        String serverAddress = Objects.requireNonNullElse(System.getenv("server"), "127.0.0.1");
        String _port = System.getenv("port");
        int port = _port == null || _port.isBlank() ? 9999 : Integer.parseInt(_port);

        try {
            socket = new Socket(serverAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 수신 데이터 처리 S
        Thread inputTh = new Thread(() -> {
            try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {

                while(true) {
                    if (socket == null || socket.isClosed() || handler == null) {
                        Thread.currentThread().yield();
                        break;
                    }

                    String json = dis.readUTF();
                    SocketData data = om.readValue(json, SocketData.class);
                    handler.accept(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        inputTh.start();
        // 수신 데이터 처리 E

    }

    public void send(SocketData data) {
        data.setFrom(name); // 사용자 고정
        data.setRegDt(LocalDateTime.now()); // 전송 시간 고정

        try {
            String json = om.writeValueAsString(data);

            Thread th = new Thread(() -> {
                try {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    dos.writeUTF(json);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            th.start();

        } catch (JsonProcessingException e) {
           e.printStackTrace();
        }
    }


}
