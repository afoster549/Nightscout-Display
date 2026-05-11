package src.logic;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

import src.Main;

public class UpdateLoop implements Runnable {
    static Sha1 sha_1 = new Sha1();

    private JLabel bgLabel;
    private Map<String, String> settingsList;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final Map<String, String> DIRECTIONS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("TripleUp", "⤊");
        map.put("DoubleUp", "⇈︎");
        map.put("SingleUp", "↑︎");
        map.put("FortyFiveUp", "↗︎");
        map.put("Flat", "→︎");
        map.put("FortyFiveDown", "↘︎");
        map.put("SingleDown", "↓︎");
        map.put("DoubleDown", "⇊");
        map.put("TripleDown", "⤋");
        map.put("NON COMPUTABLE", "-");
        map.put("RATE OUT OF RANGE", "⇕");
        map.put("NONE", "⇼");
        DIRECTIONS = Collections.unmodifiableMap(map);
    }

    public UpdateLoop(JLabel bgLabel, Map<String, String> settingsList) {
        this.bgLabel = bgLabel;
        this.settingsList = settingsList;
    }

    @Override
    public void run() {
        DecimalFormat df = new DecimalFormat("0.0");

        while (true) {
            double veryHighBg = Double.parseDouble(settingsList.get("veryHighBg"));
            double highBg = Double.parseDouble(settingsList.get("highBg"));
            double lowBg = Double.parseDouble(settingsList.get("lowBg"));
            double veryLowBg = Double.parseDouble(settingsList.get("veryLowBg"));
            boolean isMgdl = Boolean.parseBoolean(settingsList.get("mgdl"));

            String time = LocalTime.now().format(TIME_FORMATTER);
            HttpURLConnection connection = null;
            int responseCode = 0;

            try {
                boolean useHttp = Boolean.parseBoolean(settingsList.get("useHttp"));
                String protocol = useHttp ? "http" : "https";
                URL url = new URI(protocol + "://" + settingsList.get("nsurl") + "/api/v1/entries.json?count=1").toURL();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("API-SECRET", Sha1.hash(settingsList.get("apiSecret")));
                responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String[] response;
                    try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line = input.readLine();
                        response = line != null ? line.split(",") : new String[0];
                    }
                    
                    double bg = 0;
                    String direction = "⇼";

                    for (String value : response) {
                        if (value.contains("sgv") && !value.contains("type")) {
                            bg = Double.parseDouble(value.substring(6));
                            if (!isMgdl) {
                                bg /= 18;
                            }
                        } else if (value.contains("direction")) {
                            direction = value.substring(13, value.length() - 1);
                        }
                    }

                    if (bg >= veryHighBg || bg <= veryLowBg) {
                        bgLabel.setForeground(Color.RED);
                    } else if (bg >= highBg || bg <= lowBg) {
                        bgLabel.setForeground(Color.YELLOW);
                    } else {
                        bgLabel.setForeground(Color.GREEN);
                    }

                    bgLabel.setText(df.format(bg) + DIRECTIONS.getOrDefault(direction, "⇼"));

                    Main.currentStatusText = "Status: OK (" + time + ")";
                    Main.currentStatusColor = Color.GREEN;

                    if (Main.statusLabel != null) {
                        Main.statusLabel.setText(Main.currentStatusText);
                        Main.statusLabel.setForeground(Main.currentStatusColor);
                    }

                    if (Main.trayIcon != null) {
                        Main.trayIcon.setToolTip("Nightscout: OK | BG: " + df.format(bg) + " " + DIRECTIONS.getOrDefault(direction, "⇼") + " | Updated: " + time);
                    }
                } else {
                    handleErrorState(responseCode, time);
                }
            } catch (Exception error) {
                error.printStackTrace();
                handleErrorState(responseCode, time);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            try {
                Thread.sleep(Integer.parseInt(settingsList.get("checkInterval")) * 60000L);
            } catch (NumberFormatException error) {
                error.printStackTrace();
            } catch (InterruptedException e) {
                // Thread interrupted to force an immediate update check
            }
        }
    }

    private void handleErrorState(int responseCode, String time) {
        bgLabel.setText("Err");
        bgLabel.setForeground(Color.RED);

        Main.currentStatusText = "Status: Error (" + responseCode + ")";
        Main.currentStatusColor = Color.RED;

        if (Main.statusLabel != null) {
            Main.statusLabel.setText(Main.currentStatusText);
            Main.statusLabel.setForeground(Main.currentStatusColor);
        }

        if (Main.trayIcon != null) {
            Main.trayIcon.setToolTip("Nightscout: Error (" + responseCode + ") | Last attempt: " + time);
        }
    }
}