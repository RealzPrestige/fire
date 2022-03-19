package dev.zprestige.fire.module.client;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Module;
import net.minecraft.client.Minecraft;

public class RPC extends Module {

    @Override
    public void onEnable(){
        Main.discordRPCManager.start();
    }

    @Override
    public void onDisable(){
        Main.discordRPCManager.stop();
    }
}
