package ui;

import javax.swing.*;
import java.awt.*;

public class ChatFrame {
    public static ChatFrame chatFrame = new ChatFrame();
    JFrame MainFrame = new JFrame("聊天室");

    public ChatFrame(){
        InitMainFrame();
    }

    public void InitMainFrame(){
        MainFrame.setSize(400, 250);
        MainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainFrame.setLocationRelativeTo(null);
        MainFrame.setResizable(true);
        MainFrame.setFont(new Font("宋体", Font.PLAIN, 14));
    }
}
