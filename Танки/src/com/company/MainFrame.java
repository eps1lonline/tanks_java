package com.company;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private int w = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int h = Toolkit.getDefaultToolkit().getScreenSize().height;

    public MainFrame() {
        JFrame startFrame = new JFrame("Танки by epsilonline");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setBounds(0, 0, w, h);
        startFrame.setVisible(true);

        startFrame.add(new GameField()).requestFocus();
    }
}
