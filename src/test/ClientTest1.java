package test;

import msg.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ClientTest1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client1 = new Client(1, "zach", ServerConfig.serverConfig.Host, ServerConfig.serverConfig.Port);

        TimeUnit.SECONDS.sleep(3);

        client1.SendMsg(2, Message.MsgType.CLIENT_CLIENT_MESSAGE, "你好朋友");

        TimeUnit.SECONDS.sleep(3);

        client1.SendMsg(2, Message.MsgType.CLIENT_CLIENT_MESSAGE, "我是客户端1");
    }
}
