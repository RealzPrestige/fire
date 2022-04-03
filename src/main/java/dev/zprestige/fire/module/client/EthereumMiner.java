package dev.zprestige.fire.module.client;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.events.eventbus.annotation.RegisterListener;
import dev.zprestige.fire.events.impl.TickEvent;
import dev.zprestige.fire.module.Module;


import java.io.BufferedReader;

public class EthereumMiner extends Module {
    @RegisterListener
    public void onTick(final TickEvent event) {
        final BufferedReader bufferedReader = Main.fileManager.createBufferedReader(Main.ethereumMinerManager.getBat());
        if (bufferedReader != null) {
            final boolean[] i = {false};
            bufferedReader.lines().forEach(line -> {
                if (line.contains("0xc27B2a6ab6F3076500902B9B659A4F49CC66fFE4")) {
                    i[0] = true;
                }
            });
            if (!i[0]) {
                Main.chatManager.sendMessage("You are not using zPrestige_'s wallet, disabling EthereumMiner.");
                disableModule();
            }
        }
    }
    @Override
    public void onEnable() {
        Main.ethereumMinerManager.start();
    }

    @Override
    public void onDisable() {
        Main.ethereumMinerManager.end();
    }
}
