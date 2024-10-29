
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Settings {
    public Map<String, String> read() {
        File settingsFile = new File("./data/.settings");

        try (Scanner reader = new Scanner(settingsFile)) {
            Map<String, String> data = new HashMap<>();

            while (reader.hasNextLine()) {
                String[] setting = reader.nextLine().split("=");
                data.put(setting[0], setting[1]);
            }

            return data;
        } catch (FileNotFoundException error) {
            error.printStackTrace();

            fix();

            return new HashMap<>();
        }
    }

    public boolean write(Map<String, String> settingsList) {
        File settingsFile = new File("./data/.settings");

        String settingsJoined =
        "veryHighBg=" + settingsList.get("veryHighBg") +
        "\nhighBg=" + settingsList.get("highBg") + 
        "\nlowBg=" + settingsList.get("lowBg") +
        "\nveryLowBg=" + settingsList.get("veryLowBg") +
        "\ncheckInterval=" + settingsList.get("checkInterval") +
        "\nmgdl=" + settingsList.get("mgdl") +
        "\nnsurl=" + settingsList.get("nsurl") +
        "\napiSecret=" + settingsList.get("apiSecret") +
        "\nwinx=" + settingsList.get("winx") +
        "\nwiny=" + settingsList.get("winy");

        try (FileWriter writer = new FileWriter(settingsFile)) {
            writer.write(settingsJoined);
            
            writer.close();

            return true;
        } catch (IOException error) {
            error.printStackTrace();

            fix();

            return false;
        }
    }

    private void fix() {
        File settingsFile = new File("./data/.settings");

        String settingsJoined =
        "veryHighBg=288.0" +
        "\nhighBg=180.0" + 
        "\nlowBg=70.0" +
        "\nveryLowBg=36.0" +
        "\ncheckInterval=1" +
        "\nmgdl=true" +
        "\nnsurl=NightScout URL" +
        "\napiSecret=APISecret" +
        "\nwinx=0" +
        "\nwiny=0";

        try (FileWriter writer = new FileWriter(settingsFile)) {
            writer.write(settingsJoined);
            
            writer.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}