package ui;

import comm.Message;
import netSrv.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientFrame extends Thread {
    JFrame MainFrame = new JFrame("聊天室");

    FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 10, 10);
    private Client client;
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
        this.client = client;
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
                    String content = sendText.getText().split(":")[1];
                    if (!content.equals("")) {
                        try {
                            client.SendMsg(SendTo, Message.MsgType.CLIENT_CLIENT_MESSAGE, content);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        receiveText.append(client.getNickName() + ": " + sendText.getText() + "\n");
                        sendText.setText("");
                    }
                }
            }
        });
        ChatPanel.add(sendButton);
        MainFrame.add(ChatPanel);

        // 刷新消息界面thread
        RefreshRecvMsg refreshRecvMsg = new RefreshRecvMsg();
        refreshRecvMsg.start();
    }

    private class RefreshRecvMsg extends Thread {
        // msg ---> split ---> server: friend list; client:msg resource
        private String ReturnMsgSender(String msg) {
            return msg.split(":")[0];
        }

        private String ReturnMsgContent(String msg) {
            return msg.split(":")[1];
        }

        private void ReloadFriendList() {
            listModel.clear();
            friendList = friendList.replace("[", "").replace("]", "");
            String[] friends = friendList.split(", ");
            Pattern pattern = Pattern.compile("[0-9]+");
            for (String friend : friends) {
                //System.out.println("read friend is:" + friend);
                Matcher matcher = pattern.matcher(friend);
                if (matcher.matches()) {
                    System.out.println("friends : " + friend);
                    listModel.addElement(friend);
                    return;
                }
            }
        }

        public void run() {
            while (true) {
                //TODO 对accept msg的处理
                receiveText.append(client.Transfer2ClientMsg(client.getAcceptClientMsg())+"\n");
                try {
                    sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                ReloadFriendList();
            }
        }
    }
}
