package ui;

import netSrv.Client;
import netSrv.SocketManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ClientFrame extends Thread{
    JFrame MainFrame = new JFrame("聊天室");

    FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 10, 10);
    private Client client;

    //个人信息面板
    JPanel userInfoPanel = new JPanel();
    JLabel userInfoLabel = new JLabel();
    //好友面板
    JPanel FriendPanel = new JPanel();
    DefaultListModel<String> listModel = new DefaultListModel<>();

    JList<String> friendJList;

    JPanel ChatPanel = new JPanel();
    //主聊天面板
    JTextArea receiveText = new JTextArea(12,34);
    JScrollPane recvMsgScrollPane = new JScrollPane(receiveText);

    JTextArea sendText = new JTextArea();
    JScrollPane sendMsgScrollPane = new JScrollPane(sendText);

    JButton refresh = new JButton("刷新");
    JButton sendButton = new JButton("发送");

    public void run(){
        this.MainFrame.setVisible(true);
    }

    public ClientFrame(Client client) {
        InitMainFrame();
        this.client = client;
        userInfoLabel.setText("您好，用户id: " + this.client.getID() + ", 昵称: " + this.client.getNickName());
    }

    public void InitMainFrame() {
        MainFrame.setSize(630, 450);
        MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainFrame.setFont(new Font("宋体", Font.PLAIN, 14));
        MainFrame.setResizable(false);

        userInfoPanel.setBorder(BorderFactory.createTitledBorder("个人信息"));
        userInfoPanel.setLayout(fl);
        userInfoPanel.add(userInfoLabel);
        userInfoPanel.setBounds(0,0,630,50);
        MainFrame.add(userInfoPanel);

        FriendPanel.setBorder(BorderFactory.createTitledBorder("好友信息"));
        friendJList = new JList<String>(listModel);
        friendJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] indices = friendJList.getSelectedIndices();
                // 获取选项数据的 ListModel
                ListModel<String> listModel = friendJList.getModel();
                // 输出选中的选项
                for (int index : indices) {
                    sendText.setText("@"+listModel.getElementAt(index)+" ");
                    System.out.println("选中: " + listModel.getElementAt(index));

                }
            }
        });
        friendJList.setBounds(0,50,150,200);
        FriendPanel.add(friendJList);
        refresh.setBounds(0,300,50,30);
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click");
                listModel.clear();
                ArrayList<Integer> clients = SocketManager.socketManager.GetAllAvailableClientList();
                System.out.println(clients);
                for(Integer i : clients){
                    System.out.println(i);
                    listModel.addElement(Integer.toString(i));
                }
            }
        });
        FriendPanel.add(refresh);
        FriendPanel.setBounds(0,50,150,300);
        MainFrame.add(FriendPanel);

        ChatPanel.setBorder(BorderFactory.createTitledBorder("聊天页"));
        receiveText.setEditable(false);
        ChatPanel.setLayout(null);
        ChatPanel.setBounds(150,50,450,300);
        recvMsgScrollPane.setBounds(150,50,450,280);
        ChatPanel.add(recvMsgScrollPane);
        sendMsgScrollPane.setBounds(160,360,330,30);
        ChatPanel.add(sendMsgScrollPane);
        sendButton.setBounds(500,360,80,30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sendText.getText().equals("")){
                    try {
                        client.DoSendMsg(-1,sendText.getText());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    receiveText.append("\n"+client.getNickName()+": "+sendText.getText());
                    sendText.setText("");
                }
            }
        });
        ChatPanel.add(sendButton);
        MainFrame.add(ChatPanel);
    }
}
