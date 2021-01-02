package test;

import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ClientTest3 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client3 = new Client(3,"zach", ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);
        TimeUnit.SECONDS.sleep(3);
        client3.SendMsg(0, Message.MsgType.CLIENT_CLIENT_MESSAGE,"你好啊，我是客户端3");
    }
}
