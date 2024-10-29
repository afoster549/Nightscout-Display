
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class Settingswindow {
    private Map<String, String> settingsList;
    private JWindow bgWindow;

    private Point mouseDownCompCoords = null;
    private boolean dragging = false;

    private boolean open = false;
    
    public Settingswindow(Map<String, String> settingsList, JWindow bgWindow) {
        this.settingsList = settingsList;
        this.bgWindow = bgWindow;
    }


    public void create() {
        if (open) {
            return;
        }

        bgWindow.toFront();

        open = true;

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame();

        ImageIcon imgIcon = new ImageIcon("./img/nsico.png");
        frame.setIconImage(imgIcon.getImage());
        frame.setPreferredSize(new Dimension(400, 260));

        int x = ((dim.width - frame.getSize().width) / 2) - 200;
        int y = ((dim.height - frame.getSize().height) / 2) - 110;

        frame.setLocation(x, y);
        frame.setUndecorated(true);
        frame.setResizable(false);

        frame.setLayout(null);

        frame.getContentPane().setBackground(new Color(17, 17, 17));

        JLabel title = new JLabel("Settings");
        title.setBackground(new Color(17, 17, 17));
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        title.setBounds(10, 7, 150, 24);

        frame.getContentPane().add(title);

        JLabel errorMessage = new JLabel("");
        errorMessage.setBackground(new Color(17, 17, 17));
        errorMessage.setForeground(Color.RED);
        errorMessage.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        errorMessage.setBounds(10, 222, 330, 24);

        frame.getContentPane().add(errorMessage);

        JTextField nsurlFeild = textFeildFactory.create("Nightscout URL", 10, 40, 150, 20);
        nsurlFeild.setText(settingsList.get("nsurl"));
        frame.getContentPane().add(nsurlFeild);

        JTextField apiSecretFeild = textFeildFactory.create("API_SECRET", 10, 70, 150, 20);
        apiSecretFeild.setText(settingsList.get("apiSecret"));
        frame.getContentPane().add(apiSecretFeild);

        JTextField veryHighBgFeild = textFeildFactory.create("Very High BG", 10, 100, 150, 20);
        veryHighBgFeild.setText(settingsList.get("veryHighBg"));
        frame.getContentPane().add(veryHighBgFeild);

        JTextField highBgFeild = textFeildFactory.create("High BG", 10, 130, 150, 20);
        highBgFeild.setText(settingsList.get("highBg"));
        frame.getContentPane().add(highBgFeild);

        JTextField lowBgFeild = textFeildFactory.create("Low BG", 10, 160, 150, 20);
        lowBgFeild.setText(settingsList.get("lowBg"));
        frame.getContentPane().add(lowBgFeild);

        JTextField veryLowBgFeild = textFeildFactory.create("Very Low BG", 10, 190, 150, 20);
        veryLowBgFeild.setText(settingsList.get("veryLowBg"));
        frame.getContentPane().add(veryLowBgFeild);

        JTextField checkInterval = textFeildFactory.create("Check Interval (mins)", 240, 100, 150, 20);
        checkInterval.setText(settingsList.get("checkInterval"));
        frame.getContentPane().add(checkInterval);

        JButton dragBgWindow = new JButton("Drag BG window");
        dragBgWindow.setBackground(new Color(25, 25, 25));
        dragBgWindow.setForeground(Color.WHITE);
        dragBgWindow.setBounds(240, 130, 150, 20);
        dragBgWindow.setFocusable(false);

        Border paddingBorder = new EmptyBorder(0, 4, 0, 4);

        dragBgWindow.setBorder(paddingBorder);

        dragBgWindow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (dragging) {
                    bgWindow.setBackground(new Color(0, true));
                    
                    dragBgWindow.setText("Drag BG window");
                    
                    dragging = false;
                } else {
                    bgWindow.setBackground(Color.WHITE);
                    bgWindow.requestFocus();

                    dragBgWindow.setText("Stop draging window");

                    dragging = true;
                }
            }
        });

        JTextField bgWinX = textFeildFactory.create("BG Window X", 240, 160, 150, 20);
        bgWinX.setText(settingsList.get("winx"));
        frame.getContentPane().add(bgWinX);

        JTextField bgWinY = textFeildFactory.create("BG Window Y", 240, 190, 150, 20);
        bgWinY.setText(settingsList.get("winy"));
        frame.getContentPane().add(bgWinY);

        bgWindow.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (dragging) {
                    mouseDownCompCoords = event.getPoint();
                }
            }
        });

        bgWindow.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent event) {
                if (dragging) {
                    Point currCoords = event.getLocationOnScreen();
                    bgWindow.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);

                    bgWinX.setText(String.valueOf(Math.round(bgWindow.getLocation().getX())));
                    bgWinY.setText(String.valueOf(Math.round(bgWindow.getLocation().getY())));
                }
            }
        });

        frame.getContentPane().add(dragBgWindow);

        JRadioButton mgdlButton = new JRadioButton("mg/dl");
        JRadioButton mmollButton = new JRadioButton("mmol/L");

        mgdlButton.setBounds(240, 40, 150, 20);
        mmollButton.setBounds(240, 70, 150, 20);

        mgdlButton.setBackground(new Color(25, 25, 25));
        mgdlButton.setForeground(Color.WHITE);

        mmollButton.setBackground(new Color(25, 25, 25));
        mmollButton.setForeground(Color.WHITE);

        mgdlButton.setFocusable(false);
        mmollButton.setFocusable(false);

        frame.getContentPane().add(mgdlButton);
        frame.getContentPane().add(mmollButton);

        ButtonGroup unitGroup = new ButtonGroup();
        unitGroup.add(mgdlButton);
        unitGroup.add(mmollButton);

        if (settingsList.get("mgdl") == "true") {
            mgdlButton.setSelected(true);
        } else {
            mmollButton.setSelected(true);
        }

        Border noBorder = new EmptyBorder(0, 0, 0, 0);

        JButton saveSettings = new JButton("✓");
        saveSettings.setBackground(new Color(25, 25, 25));
        saveSettings.setForeground(Color.GREEN);
        saveSettings.setBounds(360, 220, 30, 30);
        saveSettings.setFocusable(false);

        saveSettings.setBorder(noBorder);

        frame.getContentPane().add(saveSettings);

        saveSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                settingsList.put("veryHighBg", veryHighBgFeild.getText());
                settingsList.put("highBg", highBgFeild.getText());
                settingsList.put("lowBg", lowBgFeild.getText());
                settingsList.put("veryLowBg", veryLowBgFeild.getText());
                settingsList.put("checkInterval", checkInterval.getText());
                settingsList.put("mgdl", String.valueOf(mgdlButton.isSelected()));
                settingsList.put("nsurl", nsurlFeild.getText());
                settingsList.put("apiSecret", apiSecretFeild.getText());
                settingsList.put("winx", bgWinX.getText());
                settingsList.put("winy", bgWinY.getText());

                Settings settings = new Settings();

                if (settings.write(settingsList)) {
                    frame.dispose();

                    open = false;
                } else {
                    errorMessage.setText("Failed to save settings.");
                };
            }
        });

        JButton discardSettings = new JButton("╳");
        discardSettings.setBackground(new Color(25, 25, 25));
        discardSettings.setForeground(Color.RED);
        discardSettings.setBounds(320, 220, 30, 30);
        discardSettings.setFocusable(false);

        discardSettings.setBorder(noBorder);

        frame.getContentPane().add(discardSettings);

        discardSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dragging = false;

                bgWindow.setBackground(new Color(0, true));
                bgWindow.setLocation(Integer.valueOf(settingsList.get("winx")), Integer.valueOf(settingsList.get("winy")));

                frame.dispose();

                open = false;
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
