package ui;

import config.ServerConfig;
import netSrv.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ServerMonitor {
    public static ServerMonitor serverMonitor = new ServerMonitor();

    private Server server;
    JFrame ServerFrame = new JFrame("服务器");

    public ServerMonitor() {

    }

    public void InitServerFrame() throws IOException {
        server = new Server(ServerConfig.serverConfig.ServerName, ServerConfig.serverConfig.Host
                , ServerConfig.serverConfig.Port, ServerConfig.serverConfig.MaxConnNum);
        ServerFrame.setTitle("Server");//设置窗体标题
        ServerFrame.setSize(400, 250);//设置窗体大小，只对顶层容器生效
        ServerFrame.setDefaultCloseOperation(3);//设置窗体关闭操作，3表示关闭窗体退出程序
        ServerFrame.setLocationRelativeTo(null);//设置窗体相对于另一组间的居中位置，参数null表示窗体相对于屏幕的中央位置
        ServerFrame.setResizable(true);//禁止调整窗体大小
        ServerFrame.setFont(new Font("宋体", Font.PLAIN, 14));//设置字体，显示格式正常，大小
        server.Start();
        ServerFrame.setVisible(true);
    }
}
