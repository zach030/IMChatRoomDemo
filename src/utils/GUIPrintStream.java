package utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.OutputStream;
import java.io.PrintStream;

public class GUIPrintStream extends PrintStream {
    private JTextComponent component;
    private StringBuffer sb = new StringBuffer();
    public GUIPrintStream(OutputStream out, JTextComponent component){
        super(out);
        this.component = component;
    }
    /** *//**
 　　* 重写write()方法，将输出信息填充到GUI组件。
 　　* @param buf
 　　* @param off
 　　* @param len
 　　*/
    public void write(byte[] buf, int off, int len){
        final String message = new String(buf, off, len);
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                sb.append(message);
                component.setText(sb.toString());
            }
        });
    }
}