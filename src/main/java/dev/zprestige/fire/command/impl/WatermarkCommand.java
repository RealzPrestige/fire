package dev.zprestige.fire.command.impl;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.command.Command;

public class WatermarkCommand extends Command {

    public WatermarkCommand(){
        super("watermark", "Watermark <Watermark>");
    }

    @Override
    public void listener(final String string){
        try {
            final String[] split = string.split(" ");
            final String watermark = split[1];
            Main.name = watermark;
            Main.chatManager.setPrefix(watermark);
            completeMessage("set watermark to " + watermark);
        } catch (Exception ignored){
            throwException(format);
        }
    }
}
