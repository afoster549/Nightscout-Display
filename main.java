
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.JWindow;

class window {
    static Settings settings = new Settings();

    public static void main(String[] args) throws InterruptedException, IOException {
        Map<String, String> settingsList = settings.read();

        BgWindow bgWindowClass = new BgWindow(settingsList);

        JWindow bgWindow = bgWindowClass.create();

        Settingswindow settingsWindowClass = new Settingswindow(settingsList, bgWindow);

        SystemTray systemTray = SystemTray.getSystemTray();
        Image trayImage = Toolkit.getDefaultToolkit().getImage("./img/nsico.png");

        PopupMenu popupMenu = new PopupMenu();
        MenuItem settingsItem = new MenuItem("Settings");

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

        TrayIcon trayIcon = new TrayIcon(trayImage, "Nightscout Display", popupMenu);
        trayIcon.setImageAutoSize(true);

        trayIcon.setPopupMenu(popupMenu);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException error) {
            System.err.println(error);
        }
    }
}