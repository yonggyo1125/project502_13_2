package org.choongang.global.socket;

import org.junit.jupiter.api.Test;

import java.net.Socket;

public class SocketTest {
    @Test
    void connectTest() throws Exception {
        Socket socket = new Socket("127.0.0.1", 3000);
        System.out.println(socket);
    }
}
