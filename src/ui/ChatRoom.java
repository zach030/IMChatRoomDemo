package ui;

import java.io.IOException;
public class ChatRoom {
    public static void main(String[] args) throws IOException {
        Login.login.initLoginFrame();
        ServerMonitor.serverMonitor.InitServerFrame();
    }
}
