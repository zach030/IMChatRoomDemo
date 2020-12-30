package ui;

import netSrv.Client;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientFrame extends Thread {
    JFrame MainFrame = new JFrame("聊天室");

    FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 10, 10);
    private static Client client;
    int SendTo = -1;
    static String friendList = "";
    //个人信息面板
    JPanel userInfoPanel = new JPanel();
    JLabel userInfoLabel = new JLabel();
    //好友面板
    JPanel FriendPanel = new JPanel();
    static DefaultListModel<String> listModel = new DefaultListModel<>();

    static JList<String> friendJList;

    JPanel ChatPanel = new JPanel();
    //主聊天面板
    private static JTextArea receiveText = new JTextArea(12, 34);
    JScrollPane recvMsgScrollPane = new JScrollPane(receiveText);

    JTextArea sendText = new JTextArea();
    JScrollPane sendMsgScrollPane = new JScrollPane(sendText);
    JButton sendButton = new JButton("发送");

    public void run() {
        this.MainFrame.setVisible(true);
    }

    public ClientFrame(Client client) {
        ClientFrame.client = client;
        InitMainFrame();
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
        userInfoPanel.setBounds(0, 0, 630, 50);
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
                    String choose = listModel.getElementAt(index);
                    sendText.setText("@" + choose + ":");
                    System.out.println("选中: " + choose);
                    SendTo = Integer.parseInt(choose);
                }
            }
        });
        friendJList.setBounds(0, 50, 150, 200);
        FriendPanel.add(friendJList);
        FriendPanel.setBounds(0, 50, 150, 300);
        MainFrame.add(FriendPanel);

        ChatPanel.setBorder(BorderFactory.createTitledBorder("聊天页"));
        receiveText.setEditable(false);
        ChatPanel.setLayout(null);
        ChatPanel.setBounds(150, 50, 450, 300);
        recvMsgScrollPane.setBounds(150, 50, 450, 280);
        ChatPanel.add(recvMsgScrollPane);
        sendMsgScrollPane.setBounds(160, 360, 330, 30);
        ChatPanel.add(sendMsgScrollPane);
        sendButton.setBounds(500, 360, 80, 30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sendText.getText().equals("")) {
                    try {
                        String[] msg = sendText.getText().split(":");
                        client.DoSendMsg(SendTo, msg[1]);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    receiveText.append(client.getNickName() + ": " + sendText.getText() + "\n");
                    sendText.setText("");
                }
            }
        });
        ChatPanel.add(sendButton);
        MainFrame.add(ChatPanel);
        ReceivingMsg receivingMsg = new ReceivingMsg();
        RefreshFriendList refreshFriendList = new RefreshFriendList();
        new Thread(receivingMsg).start();
        new Thread(refreshFriendList).start();
    }

    static class ReceivingMsg extends Thread {
        public void run() {
            while (true) {
                String msg = "";
                try {
                    msg = client.GetReceivingMsg();
                    System.out.println("msg:" + msg);
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                if (!msg.equals("")) {
                    String[] fromMsg = msg.split(":");
                    if (fromMsg[0].equals("-1")) {
                        friendList = fromMsg[1];
                        continue;
                    } else {
                        fromMsg[0] = "客户端" + fromMsg[0];
                    }
                    msg = fromMsg[0] + ": " + fromMsg[1];
                    receiveText.append(msg + "\n");
                }

            }
        }
    }

    static class RefreshFriendList extends Thread {

        public void run() {
            while (true) {
                try {
                    listModel.clear();
                    if (friendList.equals("")){
                        continue;
                    }
                    friendList = friendList.replace("[","").replace("]","");
                    String[] friends = friendList.split(", ");
                    for (String friend : friends) {
                        System.out.println("read friend is:"+friend);
                        Pattern pattern = Pattern.compile("[0-9]+");
                        Matcher matcher = pattern.matcher((CharSequence) friend);
                        boolean result = matcher.matches();
                        if (result) {
                            System.out.println("friends : " + friend);
                            listModel.addElement(friend);
                        } else {
                            System.out.println("not str");
                        }
                    }
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
