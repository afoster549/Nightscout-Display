
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
                url = new URL("https://" + settingsList.get("nsurl") + "/api/v1/entries.json?count=1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException error) {
                error.printStackTrace();
            }

            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException error) {
                error.printStackTrace();
            }

            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("API-SECRET", sha1.hash(settingsList.get("apiSecret")));

            int responseCode = 0;

            try {
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
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

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader input = null;

                try {
                    input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] response = null;

                try {
                    response = input.readLine().split(",");
                } catch (IOException e) {
                    e.printStackTrace();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bgLabel.setText("Err");
                bgLabel.setForeground(Color.RED);
            }

            try {
                Thread.sleep(Integer.valueOf(settingsList.get("checkInterval")) * 60000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}