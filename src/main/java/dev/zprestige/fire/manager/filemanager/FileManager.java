package dev.zprestige.fire.manager.filemanager;

import dev.zprestige.fire.Main;
import net.minecraft.client.Minecraft;

import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileManager {
    protected final Minecraft mc = Main.mc;
    protected final String separator = File.separator;
    protected final File gameDir = registerPathAndCreate(mc.mcDataDir + separator + Main.modid + separator);

    public File getDirectory() {
        return gameDir;
    }

    public BufferedReader createBufferedReader(File file) {
        try {
            return new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file))));
        } catch (FileNotFoundException ignored) {
            return null;
        }
    }

    public void closeBufferedReader(final BufferedReader bufferedReader){
        try {
            bufferedReader.close();
        } catch (IOException ignored) {
        }
    }

    public BufferedWriter createBufferedWriter(File file){
        try {
            return  new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            return null;
        }
    }

    public void closeBufferedWriter(final BufferedWriter bufferedWriter){
        try {
            bufferedWriter.close();
        } catch (IOException ignored) {
        }
    }

    public void writeLine(BufferedWriter bufferedWriter, String line) {
        try {
            bufferedWriter.write(line + "\r\n");
        } catch (IOException ignored) {
        }
    }

    public File registerPathAndCreate(final String file) {
        final File file1 = new File(file);
        file1.mkdirs();
        return file1;
    }

    public File registerFileAndCreate(final String file) {
        final File file1 = new File(file);
        try {
            file1.createNewFile();
        } catch (IOException ignored) {
        }
        return file1;
    }
}
