package test;

import utils.GUIPrintStream;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    private JButton btnOut;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    public MainFrame() {
        initComponents();
        //重定向到通过文本组件构建的组件输出流中。
        System.setOut(new GUIPrintStream(System.out, textArea));
    }

    private void initComponents() {
        scrollPane = new JScrollPane();
        textArea = new JTextArea();
        btnOut = new JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("标准输出重定向到GUI - www.chenwei.mobi");
        textArea.setColumns(20);
        textArea.setRows(5);
        scrollPane.setViewportView(textArea);
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
        btnOut.setText("System.out.println(System.getProperties());");
        btnOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutActionPerformed(evt);
            }
        });
        getContentPane().add(btnOut, java.awt.BorderLayout.PAGE_END);
        pack();
    }

    private void btnOutActionPerformed(ActionEvent evt) {
        System.out.println(System.getProperties());
    }

    /**
     * 　　* @param args the command line arguments
     */

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}