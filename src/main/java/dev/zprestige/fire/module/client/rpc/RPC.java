package dev.zprestige.fire.module.client.rpc;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Enables activity status on discord")
public class RPC extends Module {
    public static RPC Instance;

    public RPC() {
        Instance = this;
    }

    @Override
    public void onEnable() {
        Main.discordRPCManager.start();
    }

    @Override
    public void onDisable() {
        Main.discordRPCManager.stop();
    }
}
