package src;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

class UpdateLoop implements Runnable {
    static sha1 sha_1 = new sha1();

    private JLabel bgLabel;
    private Map<String, String> settingsList;

    public UpdateLoop(JLabel bgLabel, Map<String, String> settingsList) {
        this.bgLabel = bgLabel;
        this.settingsList = settingsList;
    }

    public void run() {
        double veryHighBg = Double.valueOf(settingsList.get("veryHighBg"));
        double highBg = Double.valueOf(settingsList.get("highBg"));
        double lowBg = Double.valueOf(settingsList.get("lowBg"));
        double veryLowBg = Double.valueOf(settingsList.get("veryLowBg"));

        while (true) {
            URL url = null;

            try {
                url = new URI("http://" + settingsList.get("nsurl") + "/api/v1/entries.json?count=1").toURL();
            } catch (MalformedURLException | URISyntaxException error) {
                error.printStackTrace();
            }

            HttpURLConnection connection = null;
            int responseCode = 0;

            try {
                if (url != null) {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("accept", "application/json");
                    connection.setRequestProperty("API-SECRET", sha1.hash(settingsList.get("apiSecret")));
                    responseCode = connection.getResponseCode();
                }
            } catch (IOException error) {
                error.printStackTrace();
            }

            Map<String, String> directions = new HashMap<String, String>() {{
                put("TripleUp", "⤊");
                put("DoubleUp", "⇈︎");
                put("SingleUp", "↑︎");
                put("FortyFiveUp", "↗︎");
                put("Flat", "→︎");
                put("FortyFiveDown", "↘︎");
                put("SingleDown", "↘︎");
                put("DoubleDown", "⇊");
                put("TripleDown", "⤋");
                put("NON COMPUTABLE", "-");
                put("RATE OUT OF RANGE", "⇕");
                put("NONE", "⇼");
            }};

        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader input = null;

                try {
                    input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } catch (IOException error) {
                    error.printStackTrace();
                }

                String[] response = null;

                try {
                    response = input.readLine().split(",");
                } catch (IOException error) {
                    error.printStackTrace();
                }
                
                double bg = 0;
                String direction = "⇼";

                for (String value : response) {
                    if (value.indexOf("sgv") != -1 && value.indexOf("type") == -1) {
                        bg = Double.valueOf(value.substring(6));

                        if (Boolean.parseBoolean(settingsList.get("mgdl"))) {
                            bgLabel.setText(Double.toString(bg));
                        } else {
                            bg /= 18;
                        }
                    } else if (value.indexOf("direction") != -1) {
                        direction = value.substring(13, value.length() - 1);
                    }
                }

                if (bg >= veryHighBg | bg <= veryLowBg) {
                    bgLabel.setForeground(Color.RED);
                } else if (bg >= highBg | bg <= lowBg) {
                    bgLabel.setForeground(Color.YELLOW);
                } else {
                    bgLabel.setForeground(Color.GREEN);
                }

                DecimalFormat df = new DecimalFormat("#.#");

                bgLabel.setText(df.format(bg) + directions.get(direction));

                try {
                    input.close();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            
            Main.currentStatusText = "Status: OK (" + time + ")";
            Main.currentStatusColor = Color.GREEN;
            
            if (Main.statusLabel != null) {
                Main.statusLabel.setText(Main.currentStatusText);
                Main.statusLabel.setForeground(Main.currentStatusColor);
            }

            if (Main.trayIcon != null) {
                Main.trayIcon.setToolTip("Nightscout: OK | BG: " + df.format(bg) + " " + directions.get(direction) + " | Updated: " + time);
            }
            } else {
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

            try {
                Thread.sleep(Integer.valueOf(settingsList.get("checkInterval")) * 60000);
            } catch (NumberFormatException error) {
                error.printStackTrace();
            } catch (InterruptedException e) {
                // Thread interrupted to force an immediate update check
            }
        }
    }
}