package dev.zprestige.fire.module.client.customfont;

import dev.zprestige.fire.module.Descriptor;
import dev.zprestige.fire.module.Module;

@Descriptor(description = "Changes the way font looks")
public class CustomFont extends Module {
    public static CustomFont Instance;

    public CustomFont() {
        Instance = this;
        setEnabled(true);
    }
}
