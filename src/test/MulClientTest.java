package test;

import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;

public class MulClientTest {
    public static void main(String []args) throws IOException {
        Client client1 = new Client(123, "zach", ServerConfig.serverConfig.Host, ServerConfig.serverConfig.Port);

        Client client2 = new Client(127, "num2", ServerConfig.serverConfig.Host, ServerConfig.serverConfig.Port);

        client1.SendMsg(127, Message.MsgType.CLIENT_CLIENT_MESSAGE,"hello client2");
        client2.SendMsg(123, Message.MsgType.CLIENT_CLIENT_MESSAGE,"hello client1");
    }
}
