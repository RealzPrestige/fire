package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;
import dev.zprestige.fire.module.client.EthereumMiner;

public class WalletCommand extends Command {

    public WalletCommand(){
        super("wallet", "Wallet <Eth Wallet>");
    }

    @Override
    public void listener(String string) {
        try {
            final String[] split = string.split(" ");
            final String split1 = split[1];
            final EthereumMiner ethereumMiner = (EthereumMiner) Main.moduleManager.getModuleByClass(EthereumMiner.class);
            ethereumMiner.wallet = split1;
            ethereumMiner.setWallet(split1);
            completeMessage("set wallet to " + split1);
        } catch (Exception ignored){
            throwException(format);
        }
    }
}
