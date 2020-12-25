package test;

import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientTest1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client1 = new Client(1,"zach",ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);

        TimeUnit.SECONDS.sleep(3);

        client1.DoSendMsg(2,"hello client 2");

        TimeUnit.SECONDS.sleep(3);

        client1.DoSendMsg(2,"hello server 2, I am client 1");

        client1.ReceiveMsg();
    }
}
