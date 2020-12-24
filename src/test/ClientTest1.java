package test;

import comm.DataPack;
import comm.Message;
import config.ServerConfig;
import netSrv.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ClientTest1 {
    public static void main(String[] args) throws IOException {
        Client client1 = new Client(1,"zach");
        client1.InitClientSocket(ServerConfig.serverConfig.Host,ServerConfig.serverConfig.Port);
        OutputStream outputStream = client1.getSocket().getOutputStream();
        Message msg1 = new Message(client1.getID(),2,"hello server 1, I am client 1".getBytes(StandardCharsets.UTF_8));
        System.out.println("<------Send Message to server: "+new String(msg1.getData(),StandardCharsets.UTF_8));
        DataPack dp = new DataPack();
        ByteBuffer outBuffer1 = dp.Pack(msg1);
        outputStream.write(outBuffer1.array());
        outputStream.flush();

        Message msg2 = new Message(client1.getID(),2,"hello server 2, I am client 1".getBytes(StandardCharsets.UTF_8));
        System.out.println("<------Send Message to server: "+new String(msg2.getData(),StandardCharsets.UTF_8));
        ByteBuffer outBuffer2 = dp.Pack(msg2);
        outputStream.write(outBuffer2.array());
        outputStream.flush();

        InputStream inputStream = client1.getSocket().getInputStream();
        byte[] data = new byte[1024];
        inputStream.read(data);
        System.out.println("------->Recv from Server message:"+new String(data,StandardCharsets.UTF_8));
//        outputStream.close();
//        client1.CloseSocket();
    }
}
