package src.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Settings {
    private Map<String, String> getDefaultSettings() {
        Map<String, String> defaultData = new HashMap<>();
        defaultData.put("veryHighBg", "16.0");
        defaultData.put("highBg", "10.0");
        defaultData.put("lowBg", "3.9");
        defaultData.put("veryLowBg", "2.0");
        defaultData.put("checkInterval", "1");
        defaultData.put("mgdl", "false");
        defaultData.put("nsurl", "");
        defaultData.put("apiSecret", "");
        defaultData.put("winx", "900");
        defaultData.put("winy", "500");
        defaultData.put("useHttp", "false");
        defaultData.put("alignRight", "false");
        defaultData.put("runOnStartup", "false");
        return defaultData;
    }

    public Map<String, String> read() {
        File settingsFile = new File("data", ".settings");

        try (Scanner reader = new Scanner(settingsFile)) {
            Map<String, String> data = getDefaultSettings();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                int index = line.indexOf('=');
                if (index != -1) {
                    data.put(line.substring(0, index), line.substring(index + 1));
                }
            }

            return data;
        } catch (FileNotFoundException error) {
            fix();

            return loadDefaultOrRead(settingsFile);
        }
    }

    private Map<String, String> loadDefaultOrRead(File settingsFile) {
        Map<String, String> data = getDefaultSettings();
        try (Scanner reader = new Scanner(settingsFile)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                int index = line.indexOf('=');
                if (index != -1) {
                    data.put(line.substring(0, index), line.substring(index + 1));
                }
            }
            return data;
        } catch (FileNotFoundException e) {
            return data;
        }
    }

    public boolean write(Map<String, String> settingsList) {
        File settingsFile = new File("data", ".settings");

        File parentDir = settingsFile.getAbsoluteFile().getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

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
        "\nwiny=" + settingsList.get("winy") +
        "\nuseHttp=" + settingsList.get("useHttp") +
        "\nalignRight=" + (settingsList.get("alignRight") != null ? settingsList.get("alignRight") : "false") +
        "\nrunOnStartup=" + (settingsList.get("runOnStartup") != null ? settingsList.get("runOnStartup") : "false");

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
        File settingsFile = new File("data", ".settings");

        File parentDir = settingsFile.getAbsoluteFile().getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        String settingsJoined =
        "veryHighBg=16.0" +
        "\nhighBg=10.0" + 
        "\nlowBg=3.9" +
        "\nveryLowBg=2.0" +
        "\ncheckInterval=1" +
        "\nmgdl=false" +
        "\nnsurl=" +
        "\napiSecret=" +
        "\nwinx=900" +
        "\nwiny=500" +
        "\nuseHttp=false" +
        "\nalignRight=false" +
        "\nrunOnStartup=false";

        try (FileWriter writer = new FileWriter(settingsFile)) {
            writer.write(settingsJoined);
            
            writer.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}