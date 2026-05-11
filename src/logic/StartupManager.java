package src.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class StartupManager {

    private static final String APP_NAME = "NightscoutDisplay";

    public static void setRunOnStartup(boolean enable) {
        // Gets the absolute path of the current jpackage executable
        String execPath = ProcessHandle.current().info().command().orElse(null);
        
        if (execPath == null) {
            System.err.println("Could not determine executable path.");
            return;
        }

        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                handleWindowsStartup(enable, execPath);
            } else if (os.contains("mac")) {
                handleMacStartup(enable, execPath);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                handleLinuxStartup(enable, execPath);
            } else {
                System.err.println("Unsupported OS for startup management.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleWindowsStartup(boolean enable, String execPath) throws IOException {
        if (enable) {
            new ProcessBuilder("reg", "add", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", APP_NAME, "/t", "REG_SZ", "/d", "\"" + execPath + "\"", "/f").start();
        } else {
            new ProcessBuilder("reg", "delete", "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run", "/v", APP_NAME, "/f").start();
        }
    }

    private static void handleLinuxStartup(boolean enable, String execPath) throws IOException {
        File autostartDir = new File(System.getProperty("user.home"), ".config/autostart");
        if (!autostartDir.exists()) {
            autostartDir.mkdirs();
        }
        
        File desktopFile = new File(autostartDir, APP_NAME + ".desktop");
        
        if (enable) {
            try (FileWriter writer = new FileWriter(desktopFile)) {
                writer.write("[Desktop Entry]\n");
                writer.write("Type=Application\n");
                writer.write("Name=Nightscout Display\n");
                writer.write("Exec=\"" + execPath + "\"\n");
                writer.write("Terminal=false\n");
            }
        } else {
            Files.deleteIfExists(desktopFile.toPath());
        }
    }

    private static void handleMacStartup(boolean enable, String execPath) throws IOException {
        File launchAgentsDir = new File(System.getProperty("user.home"), "Library/LaunchAgents");
        if (!launchAgentsDir.exists()) {
            launchAgentsDir.mkdirs();
        }
        
        File plistFile = new File(launchAgentsDir, "com.nightscout.display.plist");
        
        if (enable) {
            try (FileWriter writer = new FileWriter(plistFile)) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n");
                writer.write("<plist version=\"1.0\">\n");
                writer.write("<dict>\n");
                writer.write("    <key>Label</key>\n");
                writer.write("    <string>com.nightscout.display</string>\n");
                writer.write("    <key>ProgramArguments</key>\n");
                writer.write("    <array>\n");
                writer.write("        <string>" + execPath + "</string>\n");
                writer.write("    </array>\n");
                writer.write("    <key>RunAtLoad</key>\n");
                writer.write("    <true/>\n");
                writer.write("</dict>\n");
                writer.write("</plist>\n");
            }
        } else {
            Files.deleteIfExists(plistFile.toPath());
        }
    }
}