package UI;

import UI.MainSettingsWindow;

import javax.swing.*;
import java.awt.*;

public class UiThread extends Thread{
    MainSettingsWindow mainSettingsWindow;

    @Override
    public void run() {
        JFrame frame=new JFrame();
        frame.setPreferredSize(new Dimension(100,100));
        frame.setLocation(700,300);
        frame.setSize(200,200);
        mainSettingsWindow=new MainSettingsWindow();
        frame.setContentPane(mainSettingsWindow.mainPanel);
        frame.setVisible(true);
    }
}
