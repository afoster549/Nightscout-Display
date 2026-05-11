package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class SettingsWindow {
    private Map<String, String> settingsList;
    private JFrame bgWindow;

    private Point mouseDownCompCoords = null;
    private boolean dragging = false;

    private boolean open = false;
    
    public SettingsWindow(Map<String, String> settingsList, JFrame bgWindow) {
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

        URL iconUrl = getClass().getResource("images/nsicon.png");
        Image windowIcon = null;
        
        try {
            if (iconUrl != null) {
                windowIcon = ImageIO.read(iconUrl);
            } else {
                File imgFile = new File("./src/images/nsicon.png");
                if (!imgFile.exists()) imgFile = new File("./images/nsicon.png");
                
                if (imgFile.exists()) windowIcon = ImageIO.read(imgFile);
            }
        } catch (IOException e) {}
        
        if (windowIcon != null) {
            frame.setIconImage(windowIcon);
        }
        frame.setPreferredSize(new Dimension(400, 380));

        int x = ((dim.width - frame.getSize().width) / 2) - 200;
        int y = ((dim.height - frame.getSize().height) / 2) - 190;

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

        Main.statusLabel = new JLabel(Main.currentStatusText);
        Main.statusLabel.setBackground(new Color(17, 17, 17));
        Main.statusLabel.setForeground(Main.currentStatusColor);
        Main.statusLabel.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        Main.statusLabel.setBounds(240, 10, 150, 20);
        frame.getContentPane().add(Main.statusLabel);

        JLabel errorMessage = new JLabel("");
        errorMessage.setBackground(new Color(17, 17, 17));
        errorMessage.setForeground(Color.RED);
        errorMessage.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        errorMessage.setBounds(10, 342, 330, 24);

        frame.getContentPane().add(errorMessage);

        JLabel nsurlLabel = new JLabel("Nightscout URL");
        nsurlLabel.setForeground(Color.WHITE);
        nsurlLabel.setBounds(10, 40, 150, 20);
        frame.getContentPane().add(nsurlLabel);
        
        JLabel useHttpLabel = new JLabel("Protocol");
        useHttpLabel.setForeground(Color.WHITE);
        useHttpLabel.setBounds(240, 40, 150, 20);
        frame.getContentPane().add(useHttpLabel);

        JCheckBox useHttpCheckbox = new JCheckBox("Use HTTP");
        useHttpCheckbox.setBounds(240, 60, 150, 20);
        useHttpCheckbox.setBackground(new Color(17, 17, 17));
        useHttpCheckbox.setForeground(Color.WHITE);
        useHttpCheckbox.setFocusable(false);
        if ("true".equals(settingsList.get("useHttp"))) {
            useHttpCheckbox.setSelected(true);
        }
        frame.getContentPane().add(useHttpCheckbox);

        JTextField nsurlFeild = textFeildFactory.create("Nightscout URL", 10, 60, 150, 20);
        nsurlFeild.setText(settingsList.get("nsurl"));
        frame.getContentPane().add(nsurlFeild);

        JLabel apiSecretLabel = new JLabel("API Secret");
        apiSecretLabel.setForeground(Color.WHITE);
        apiSecretLabel.setBounds(10, 90, 150, 20);
        frame.getContentPane().add(apiSecretLabel);
        
        JPasswordField apiSecretFeild = textFeildFactory.createPassword("API_SECRET", 10, 110, 150, 20);
        apiSecretFeild.setText(settingsList.get("apiSecret"));
        frame.getContentPane().add(apiSecretFeild);

        JLabel veryHighBgLabel = new JLabel("Very High BG");
        veryHighBgLabel.setForeground(Color.WHITE);
        veryHighBgLabel.setBounds(10, 140, 150, 20);
        frame.getContentPane().add(veryHighBgLabel);
        
        JTextField veryHighBgFeild = textFeildFactory.create("Very High BG", 10, 160, 150, 20);
        veryHighBgFeild.setText(settingsList.get("veryHighBg"));
        veryHighBgFeild.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(veryHighBgFeild);

        JLabel highBgLabel = new JLabel("High BG");
        highBgLabel.setForeground(Color.WHITE);
        highBgLabel.setBounds(10, 190, 150, 20);
        frame.getContentPane().add(highBgLabel);
        
        JTextField highBgFeild = textFeildFactory.create("High BG", 10, 210, 150, 20);
        highBgFeild.setText(settingsList.get("highBg"));
        highBgFeild.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(highBgFeild);

        JLabel lowBgLabel = new JLabel("Low BG");
        lowBgLabel.setForeground(Color.WHITE);
        lowBgLabel.setBounds(10, 240, 150, 20);
        frame.getContentPane().add(lowBgLabel);
        
        JTextField lowBgFeild = textFeildFactory.create("Low BG", 10, 260, 150, 20);
        lowBgFeild.setText(settingsList.get("lowBg"));
        lowBgFeild.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(lowBgFeild);

        JLabel veryLowBgLabel = new JLabel("Very Low BG");
        veryLowBgLabel.setForeground(Color.WHITE);
        veryLowBgLabel.setBounds(10, 290, 150, 20);
        frame.getContentPane().add(veryLowBgLabel);
        
        JTextField veryLowBgFeild = textFeildFactory.create("Very Low BG", 10, 310, 150, 20);
        veryLowBgFeild.setText(settingsList.get("veryLowBg"));
        veryLowBgFeild.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(veryLowBgFeild);

        JLabel checkIntervalLabel = new JLabel("Check Interval (mins)");
        checkIntervalLabel.setForeground(Color.WHITE);
        checkIntervalLabel.setBounds(240, 140, 150, 20);
        frame.getContentPane().add(checkIntervalLabel);
        
        JTextField checkInterval = textFeildFactory.create("Check Interval (mins)", 240, 160, 150, 20);
        checkInterval.setText(settingsList.get("checkInterval"));
        checkInterval.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(checkInterval);

        JLabel bgPositionLabel = new JLabel("BG Position");
        bgPositionLabel.setForeground(Color.WHITE);
        bgPositionLabel.setBounds(240, 190, 150, 20);
        frame.getContentPane().add(bgPositionLabel);

        JButton dragBgWindow = new JButton("Drag BG window");
        dragBgWindow.setBackground(new Color(25, 25, 25));
        dragBgWindow.setForeground(Color.WHITE);
        dragBgWindow.setBounds(240, 210, 150, 20);
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

        JLabel bgWinXLabel = new JLabel("BG Window X");
        bgWinXLabel.setForeground(Color.WHITE);
        bgWinXLabel.setBounds(240, 240, 150, 20);
        frame.getContentPane().add(bgWinXLabel);
        
        JTextField bgWinX = textFeildFactory.create("BG Window X", 240, 260, 150, 20);
        bgWinX.setText(settingsList.get("winx"));
        bgWinX.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '-') {
                    e.consume();
                }
            }
        });
        frame.getContentPane().add(bgWinX);

        JLabel bgWinYLabel = new JLabel("BG Window Y");
        bgWinYLabel.setForeground(Color.WHITE);
        bgWinYLabel.setBounds(240, 290, 150, 20);
        frame.getContentPane().add(bgWinYLabel);
        
        JTextField bgWinY = textFeildFactory.create("BG Window Y", 240, 310, 150, 20);
        bgWinY.setText(settingsList.get("winy"));
        bgWinY.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '-') {
                    e.consume();
                }
            }
        });
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

        JLabel unitLabel = new JLabel("BG Unit");
        unitLabel.setForeground(Color.WHITE);
        unitLabel.setBounds(240, 90, 150, 20);
        frame.getContentPane().add(unitLabel);

        JRadioButton mgdlButton = new JRadioButton("mg/dl");
        JRadioButton mmollButton = new JRadioButton("mmol/L");

        mgdlButton.setBounds(240, 110, 70, 20);
        mmollButton.setBounds(310, 110, 80, 20);

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

        if ("true".equals(settingsList.get("mgdl"))) {
            mgdlButton.setSelected(true);
        } else {
            mmollButton.setSelected(true);
        }

        final boolean[] currentIsMgdl = { mgdlButton.isSelected() };

        ActionListener unitChangeListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean toMgdl = mgdlButton.isSelected();
                if (toMgdl != currentIsMgdl[0]) {
                    JTextField[] fields = {veryHighBgFeild, highBgFeild, lowBgFeild, veryLowBgFeild};
                    for (JTextField field : fields) {
                        try {
                            double val = Double.parseDouble(field.getText());
                            val = (double) Math.round((toMgdl ? val * 18.0 : val / 18.0) * 10) / 10.0;
                            field.setText(String.valueOf(val));
                        } catch (NumberFormatException e) {}
                    }

                    try {
                        JLabel bgLabel = (JLabel) bgWindow.getContentPane().getComponent(0);
                        String currentText = bgLabel.getText();
                        if (!currentText.equals("Err") && !currentText.isEmpty()) {
                            String numericPart = currentText.replaceAll("[^0-9.]", "");
                            String directionPart = currentText.replaceAll("[0-9.]", "");
                            
                            double val = Double.parseDouble(numericPart);
                            val = toMgdl ? val * 18.0 : val / 18.0;
                            
                            DecimalFormat df = new DecimalFormat("0.0");
                            bgLabel.setText(df.format(val) + directionPart);

                            double vh = Double.parseDouble(veryHighBgFeild.getText());
                            double h = Double.parseDouble(highBgFeild.getText());
                            double l = Double.parseDouble(lowBgFeild.getText());
                            double vl = Double.parseDouble(veryLowBgFeild.getText());

                            if (val >= vh || val <= vl) {
                                bgLabel.setForeground(Color.RED);
                            } else if (val >= h || val <= l) {
                                bgLabel.setForeground(Color.YELLOW);
                            } else {
                                bgLabel.setForeground(Color.GREEN);
                            }
                        }
                    } catch (Exception e) {}

                    currentIsMgdl[0] = toMgdl;
                }
            }
        };

        mgdlButton.addActionListener(unitChangeListener);
        mmollButton.addActionListener(unitChangeListener);

        Border noBorder = new EmptyBorder(0, 0, 0, 0);

        JButton saveSettings = new JButton("✓");
        saveSettings.setBackground(new Color(25, 25, 25));
        saveSettings.setForeground(Color.GREEN);
        saveSettings.setBounds(360, 340, 30, 30);
        saveSettings.setFocusable(false);

        saveSettings.setBorder(noBorder);

        frame.getContentPane().add(saveSettings);

        saveSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                boolean forceCheck = !settingsList.get("nsurl").equals(nsurlFeild.getText()) || 
                                     !settingsList.get("apiSecret").equals(new String(apiSecretFeild.getPassword())) ||
                                     !settingsList.get("checkInterval").equals(checkInterval.getText()) ||
                                     !settingsList.get("mgdl").equals(String.valueOf(mgdlButton.isSelected())) ||
                                     !settingsList.get("veryHighBg").equals(veryHighBgFeild.getText()) ||
                                     !settingsList.get("highBg").equals(highBgFeild.getText()) ||
                                     !settingsList.get("lowBg").equals(lowBgFeild.getText()) ||
                                     !settingsList.get("veryLowBg").equals(veryLowBgFeild.getText()) ||
                                     !String.valueOf(useHttpCheckbox.isSelected()).equals(settingsList.get("useHttp"));

                settingsList.put("veryHighBg", veryHighBgFeild.getText());
                settingsList.put("highBg", highBgFeild.getText());
                settingsList.put("lowBg", lowBgFeild.getText());
                settingsList.put("veryLowBg", veryLowBgFeild.getText());
                settingsList.put("checkInterval", checkInterval.getText());
                settingsList.put("mgdl", String.valueOf(mgdlButton.isSelected()));
                settingsList.put("nsurl", nsurlFeild.getText());
                settingsList.put("apiSecret", new String(apiSecretFeild.getPassword()));
                settingsList.put("winx", bgWinX.getText());
                settingsList.put("winy", bgWinY.getText());
                settingsList.put("useHttp", String.valueOf(useHttpCheckbox.isSelected()));

                Settings settings = new Settings();

                if (settings.write(settingsList)) {
                    Main.statusLabel = null;
                    frame.dispose();

                    open = false;

                    if (forceCheck && Main.updateThread != null) {
                        Main.updateThread.interrupt();
                    }
                } else {
                    errorMessage.setText("Failed to save settings.");
                };
            }
        });

        JButton discardSettings = new JButton("╳");
        discardSettings.setBackground(new Color(25, 25, 25));
        discardSettings.setForeground(Color.RED);
        discardSettings.setBounds(320, 340, 30, 30);
        discardSettings.setFocusable(false);

        discardSettings.setBorder(noBorder);

        frame.getContentPane().add(discardSettings);

        discardSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dragging = false;

                bgWindow.setBackground(new Color(0, true));
                bgWindow.setLocation(Integer.valueOf(settingsList.get("winx")), Integer.valueOf(settingsList.get("winy")));

                Main.statusLabel = null;
                frame.dispose();

                open = false;
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.setVisible(true);
    }
}
