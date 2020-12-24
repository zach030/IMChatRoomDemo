package test;

import config.ServerConfig;
import netSrv.Server;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        Server server = new Server(ServerConfig.serverConfig.ServerName, ServerConfig.serverConfig.Host
                , ServerConfig.serverConfig.Port, ServerConfig.serverConfig.MaxConnNum);
        server.InitServerSocket();
        server.Start();
    }
}
