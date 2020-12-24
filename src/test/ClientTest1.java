package test;

import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ClientTest1 {
    public static void main(String[] args) throws IOException {
        Client client1 = new Client(1,"zach",ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);
        Message msg1 = client1.PrepareMsg(2,"hello client 2");
        client1.SendMsg(msg1);

        Message msg2 = client1.PrepareMsg(2,"hello server 2, I am client 1");
        client1.SendMsg(msg2);

        InputStream inputStream = client1.getSocket().getInputStream();
        byte[] data = new byte[1024];
        inputStream.read(data);
        System.out.println("------->Recv from Server message:"+new String(data,StandardCharsets.UTF_8));
//        outputStream.close();
//        client1.CloseSocket();
    }
}
