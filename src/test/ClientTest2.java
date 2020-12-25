package test;

import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;

public class ClientTest2 {
    public static void main(String[] args) throws IOException {
        Client client2 = new Client(2,"zach",ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);

        client2.ReceiveMsg();
    }
}
