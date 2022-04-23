package dev.zprestige.fire.module.client.ethereumminer;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

@Descriptor(description = "Automatically opens a program the will mine Ethereum for you")
public class EthereumMiner extends Module {
    public String wallet = "NULL";

    public EthereumMiner(){
        final File file = Main.ethereumMinerManager.getBat();
        if (!file.exists()){
            return;
        }
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(Main.ethereumMinerManager.getBat());
        bufferedReader.lines().forEach(line -> {
            if (line.contains("miner")){
                final String line1 = line.replace("miner -a ethash -o stratum+tcp://eu1.ethermine.org:4444 -u ", "");
                wallet = line1.replace(".default -log", "");
            }
        });
    }

    public void setWallet(final String wallet) {
        final File file = Main.ethereumMinerManager.getBat();
        if (!file.exists()) {
            return;
        }
        final BufferedWriter bufferedWriter = Main.fileManager.createBufferedWriter(Main.ethereumMinerManager.getBat());
        Main.fileManager.writeLine(bufferedWriter, "@cd /d \"%~dp0\"");
        Main.fileManager.writeLine(bufferedWriter, "miner -a ethash -o stratum+tcp://eu1.ethermine.org:4444 -u " + wallet + ".default -log");
        Main.fileManager.writeLine(bufferedWriter, "pause");
        Main.fileManager.closeBufferedWriter(bufferedWriter);
        this.wallet = wallet;
    }

    @Override
    public void onEnable() {
        if (wallet.equals("NULL")){
            Main.chatManager.sendMessage("Wallet \"NULL\" not found, use the wallet command to specify a wallet.");
            disableModule();
            return;
        }
        Main.ethereumMinerManager.start();
    }

    @Override
    public void onDisable() {
        Main.ethereumMinerManager.end();
    }
}
