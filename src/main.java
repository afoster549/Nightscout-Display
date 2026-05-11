package src;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main {
    static Settings settings = new Settings();
    static TrayIcon trayIcon;
    static JLabel statusLabel;
    static String currentStatusText = "Status: Connecting...";
    static Color currentStatusColor = Color.WHITE;
    static Thread updateThread;

    public static void main(String[] args) throws InterruptedException, IOException {
        Map<String, String> settingsList = settings.read();


        SystemTray systemTray = SystemTray.getSystemTray();

        URL iconUrl = Main.class.getResource("images/nsicon.png");
        Image trayImage = null;
        
        try {
            if (iconUrl != null) {
                trayImage = ImageIO.read(iconUrl);
            } else {
                File imgFile = new File("./src/images/nsicon.png");
                if (!imgFile.exists()) imgFile = new File("./images/nsicon.png");
                
                if (imgFile.exists()) trayImage = ImageIO.read(imgFile);
            }
        } catch (IOException e) {}
        
        if (trayImage == null) {
            trayImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        }

        PopupMenu popupMenu = new PopupMenu();
        MenuItem settingsItem = new MenuItem("Settings");

        trayIcon = new TrayIcon(trayImage, "Nightscout Display: Connecting...", popupMenu);
        trayIcon.setImageAutoSize(true);

        BgWindow bgWindowClass = new BgWindow(settingsList);
        JFrame bgWindow = bgWindowClass.create();

        SettingsWindow settingsWindowClass = new SettingsWindow(settingsList, bgWindow);

        settingsItem.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent error) {
                settingsWindowClass.create();
            }
        });

        MenuItem exitItem = new MenuItem("Exit");

        exitItem.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent error) {
                System.exit(0);
            }
        });

        popupMenu.add(settingsItem);
        popupMenu.add(exitItem);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException error) {
            System.err.println(error);
        }
    }
}