package test;

import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientTest2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client2 = new Client(2,"zach",ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);
        TimeUnit.SECONDS.sleep(3);
        client2.SendMsg(3, Message.MsgType.CLIENT_CLIENT_MESSAGE,"你好啊，我是客户端2");
    }
}
