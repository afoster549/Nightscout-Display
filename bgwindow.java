
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JWindow;

class BgWindow {
    private Map<String, String> settingsList;

    public BgWindow(Map<String, String> settingsList) {
        this.settingsList = settingsList;
    }

    public JWindow create() {
        JWindow window = new JWindow();
        window.setPreferredSize(new Dimension(100, 50));

        window.setAlwaysOnTop(true);
        window.setFocusable(false);
        
        window.setBackground(new Color(0, true));
        
        window.setLocation(Integer.valueOf(settingsList.get("winx")), Integer.valueOf(settingsList.get("winy")));
        
        JLabel bgLabel = new JLabel("");
        bgLabel.setFont(new Font("Ubuntu", Font.PLAIN, 24));
        bgLabel.setForeground(Color.GREEN);
        window.getContentPane().add(bgLabel);
        
        window.pack();
        
        window.setVisible(true);
        
        UpdateLoop updateLoop = new UpdateLoop(bgLabel, settingsList);

        new Thread(updateLoop).start();

        return window;
    }
}
