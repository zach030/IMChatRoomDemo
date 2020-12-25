package ui;

import java.io.IOException;
// server = new Server(ServerConfig.serverConfig.ServerName, ServerConfig.serverConfig.Host
//                , ServerConfig.serverConfig.Port, ServerConfig.serverConfig.MaxConnNum);
public class ChatRoom {
    public static void main(String[] args) throws IOException {
        Login.login.initLoginFrame();
        ServerMonitor.serverMonitor.InitServerFrame();
    }
}
