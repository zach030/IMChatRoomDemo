package ui;

import javax.swing.*;
import java.awt.*;

public class Login {
    public static Login login = new Login();

    JFrame LoginMainFrame = new JFrame("登录");
    JLabel labname = new JLabel("账号：");
    JTextField text_name = new JTextField();
    Dimension dim1 = new Dimension(300, 30);
    JButton button1 = new JButton();
    Dimension dim2 = new Dimension(100, 30);

    public Login() {

    }

    public void initLoginFrame() {
        LoginMainFrame.setSize(400, 250);//设置窗体大小，只对顶层容器生效
        LoginMainFrame.setDefaultCloseOperation(3);//设置窗体关闭操作，3表示关闭窗体退出程序
        LoginMainFrame.setLocationRelativeTo(null);//设置窗体相对于另一组间的居中位置，参数null表示窗体相对于屏幕的中央位置
        LoginMainFrame.setResizable(true);//禁止调整窗体大小
        LoginMainFrame.setFont(new Font("宋体", Font.PLAIN, 14));//设置字体，显示格式正常，大小
        //实例化FlowLayout流式布局类的对象，指定对齐方式为居中对齐组件之间的间隔为10个像素
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 10, 10);
        //实例化流式布局类的对象
        LoginMainFrame.setLayout(fl);
        labname.setFont(new Font("宋体", Font.PLAIN, 14));
        //将labname标签添加到窗体上
        LoginMainFrame.add(labname);
        text_name.setPreferredSize(dim1);//设置除顶级容器组件以外其他组件的大小
        //将textName标签添加到窗体上
        LoginMainFrame.add(text_name);
        button1.setText("登录");
        button1.setFont(new Font("宋体", Font.PLAIN, 14));
        //设置按键大小
        button1.setSize(dim2);
        LoginMainFrame.add(button1);
        LoginMainFrame.setVisible(true);//窗体可见，一定要放在所有组件加入窗体后
    }
}
