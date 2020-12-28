package ui;

import config.ServerConfig;
import netSrv.Server;
import utils.GUIPrintStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerMonitor {
    private Server server;
    JFrame ServerFrame = new JFrame("服务器");
    // server info panel
    private JPanel BaseInfoPanel = new JPanel();
    private JLabel ServerNameLabel = new JLabel("名称:");
    private JLabel ServerName = new JLabel();
    private JLabel HostLabel = new JLabel("Host:");
    private JLabel Host = new JLabel();
    private JLabel PortLabel = new JLabel("Port:");
    private JLabel Port = new JLabel();
    // control panel
    private JPanel Controller = new JPanel();
    private JButton Start = new JButton("Start");
    private JButton Stop = new JButton("Stop");
    // log panel
    private JPanel logPanel = new JPanel();
    private JTextArea logInfo = new JTextArea();
    private JScrollPane logSP = new JScrollPane(logInfo);

    FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 10, 10);

    public ServerMonitor() throws IOException {
        server = new Server(ServerConfig.serverConfig.ServerName, ServerConfig.serverConfig.Host
                , ServerConfig.serverConfig.Port, ServerConfig.serverConfig.MaxConnNum);

        ServerFrame.setSize(800, 450);//设置窗体大小，只对顶层容器生效
        ServerFrame.setLayout(new BorderLayout());
        ServerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        RunServer();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });
        Stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StopServer();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Controller.setBorder(BorderFactory.createTitledBorder("系统控制"));
        Controller.setLayout(fl);
        Controller.add(Start);
        Controller.add(Stop);
        ServerFrame.add(Controller, BorderLayout.NORTH);

        BaseInfoPanel.setBorder(BorderFactory.createTitledBorder("服务器信息"));
        BaseInfoPanel.setLayout(fl);
        BaseInfoPanel.add(ServerNameLabel);
        ServerName.setText(ServerConfig.serverConfig.ServerName);
        BaseInfoPanel.add(ServerName);

        BaseInfoPanel.add(HostLabel);
        Host.setText(ServerConfig.serverConfig.Host);
        BaseInfoPanel.add(Host);

        BaseInfoPanel.add(PortLabel);
        Port.setText(Integer.toString(ServerConfig.serverConfig.Port));
        BaseInfoPanel.add(Port);

        ServerFrame.add(BaseInfoPanel, BorderLayout.WEST);

//        logPanel.setBorder(BorderFactory.createTitledBorder("运行日志"));
//        logPanel.add(logSP);

        logPanel.setBorder(BorderFactory.createTitledBorder("运行日志"));
        logPanel.add(logSP);
        logPanel.setLayout(null);
        logSP.setBounds(20, 20, 450, 300);
        logPanel.setBounds(30, 300, 350, 220);

        logInfo.setEditable(false);
        logInfo.setFont(new Font("黑体", Font.PLAIN, 16));
        ServerFrame.add(logPanel, BorderLayout.CENTER);

        System.setOut(new GUIPrintStream(System.out, logInfo));
    }

    public void SetVisible(boolean flag) {
        this.ServerFrame.setVisible(flag);
    }

    public static void main(String[] args) throws IOException {
        ServerMonitor serverMonitor = new ServerMonitor();
        serverMonitor.SetVisible(true);
    }

    public void RunServer() throws IOException {
        server.Start();
    }

    public void StopServer() throws IOException {
        server.Stop();
    }

}
