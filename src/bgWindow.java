package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.Timer;

class BgWindow {
    private Map<String, String> settingsList;

    public BgWindow(Map<String, String> settingsList) {
        this.settingsList = settingsList;
    }

    public JFrame create() {
        JFrame window = new JFrame();
        window.setUndecorated(true);
        window.setPreferredSize(new Dimension(100, 50));

        window.setAlwaysOnTop(true);
        window.setFocusableWindowState(false);
        window.setFocusable(false);
        window.setType(java.awt.Window.Type.UTILITY);
        
        window.setBackground(new Color(0, true));
        
        window.setLocation(Integer.valueOf(settingsList.get("winx")), Integer.valueOf(settingsList.get("winy")));
        
        JLabel bgLabel = new JLabel("");
        bgLabel.setFont(new Font("Ubuntu", Font.PLAIN, 24));
        bgLabel.setForeground(Color.GREEN);
        window.getContentPane().add(bgLabel);
        
        window.pack();
        
        window.setVisible(true);
        
        Timer onTopTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setAlwaysOnTop(true);
                window.toFront();
            }
        });
        onTopTimer.start();

        UpdateLoop updateLoop = new UpdateLoop(bgLabel, settingsList);

        Main.updateThread = new Thread(updateLoop);
        Main.updateThread.start();

        return window;
    }
}
