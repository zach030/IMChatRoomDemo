package test;

import config.ServerConfig;
import netSrv.Server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server(ServerConfig.serverConfig.ServerName, ServerConfig.serverConfig.Host
                , ServerConfig.serverConfig.Port, ServerConfig.serverConfig.MaxConnNum);
        server.Start();
        TimeUnit.SECONDS.sleep(3);
        server.Stop();
    }
}
