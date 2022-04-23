package dev.zprestige.fire.ui.menu.panel;

import dev.zprestige.fire.Main;
import dev.zprestige.fire.module.Module;
import dev.zprestige.fire.settings.Setting;
import dev.zprestige.fire.util.impl.RenderUtil;
import dev.zprestige.fire.util.impl.Vector2D;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class PanelModule {
    protected final ArrayList<PanelInner> panelInners = new ArrayList<>();
    protected final PanelCategory panelCategory;
    protected Color thirdBackgroundColor, secondBackgroundColor;
    protected final Module module;
    protected float[] col = new float[]{1.0f, 1.0f, 1.0f};
    protected float x, y, width, height, a = 0.0f, scroll;
    protected final float offset = 131.6666666666667f;

    public PanelModule(final PanelCategory panelCategory, final Module module, final float x, final float y, final float width, final float height) {
        this.panelCategory = panelCategory;
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scroll = 0.0f;
        for (Setting<?> setting : module.getSettings()) {
            String panel = setting.getPanel();
            if (!doesPanelInnerListContainName(panel)) {
                PanelInner panelInner = new PanelInner(this, panel, 0, 0, offset, 0);
                panelInners.add(panelInner);
            }
        }
    }

    public void render(final int mouseX, final int mouseY) {
        if (PanelScreen.activeCategory != null && PanelScreen.activeCategory.equals(panelCategory)) {
            if (panelCategory.alpha < 0.0f || panelCategory.alpha > 1.0f) {
                return;
            }
            thirdBackgroundColor = new Color(PanelScreen.thirdBackgroundColor.getRed() / 255.0f, PanelScreen.thirdBackgroundColor.getGreen() / 255.0f, PanelScreen.thirdBackgroundColor.getBlue() / 255.0f, panelCategory.alpha);
            secondBackgroundColor = new Color(PanelScreen.secondBackgroundColor.getRed() / 255.0f, PanelScreen.secondBackgroundColor.getGreen() / 255.0f, PanelScreen.secondBackgroundColor.getBlue() / 255.0f, panelCategory.alpha);

            float y = this.y + 10 - panelCategory.alpha * 10;
            for (int i = 20; i <= 28; i = i + 2) {
                final float val = ((28 - i) / 2f);
                RenderUtil.drawRoundedRect(x, y, x + width + val, y + height + val, 10, new Color(0, 0, 0, i));
            }
            RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, thirdBackgroundColor);
            RenderUtil.drawRoundedRect(x + 1, y + 1, x + width - 1, y + height - 1, 10, secondBackgroundColor);
            try {
                Main.fontManager.drawStringWithShadow(module.getName(), new Vector2D(x + 4, y + 2 + Main.fontManager.getFontHeight() / 2.0f), new Color(col[0], col[1], col[2], 1.0f).getRGB());
            } catch (Exception ignored) {
            }

            RenderUtil.prepareScale(0.75f);
            final String description = module.getDescription();
            float delta = 0.0f;
            float deltaY1 = 0.0f;
            for (String string : description.split(" ")) {
                final float width = Main.fontManager.getStringWidth(string + " ");
                if (delta + width > this.width) {
                    delta = 0.0f;
                    deltaY1 += Main.fontManager.getFontHeight() + 0.75f;
                }
                Main.fontManager.drawStringWithShadow(string, new Vector2D((x + 4 + delta) / 0.75f, (y + 5 + Main.fontManager.getFontHeight() * 1.5f + deltaY1) / 0.75f), new Color(1.0f, 1.0f, 1.0f, inside(mouseX, mouseY) ? 0.4f : 0.5f).getRGB());
                delta += width * 0.75f;
            }
            RenderUtil.releaseScale();
            hoverColor(mouseX, mouseY, PanelScreen.animationFactor() * 10);
        }
        a = normalizeNumber(a, PanelScreen.activeModule != null && PanelScreen.activeModule.equals(this) ? 1.0f : 0.0f, PanelScreen.animationFactor() * 10);
        if (PanelScreen.activeModule != null && PanelScreen.activeModule.equals(this)) {
            int mouse = Mouse.getDWheel();
            float deltaY = PanelScreen.y + PanelScreen.secondStartY + 5;
            RenderUtil.prepareScissor((int) (PanelScreen.x + PanelScreen.secondStart + 5), (int) (deltaY - 5.0f), (int) (PanelScreen.secondWidth - 1.0f), (int) (PanelScreen.height - PanelScreen.secondStartY - PanelScreen.secondEndY - 2.0f));
            float[] floats = new float[]{deltaY, deltaY, deltaY};
            float[] xs = new float[]{PanelScreen.x + PanelScreen.secondStart + 5, PanelScreen.x + PanelScreen.secondStart + 5 + offset + 5, PanelScreen.x + PanelScreen.secondStart + 5 + (offset + 5) * 2};
            float totalHeight = 0.0f;
            for (final PanelInner panelInner : panelInners) {
                if (!PanelScreen.searchingString.equals("") && !doesPanelInnerContain(panelInner, PanelScreen.searchingString)) {
                    continue;
                }
                final TreeMap<Float, Float> floatFloatTreeMap = new TreeMap<>();
                for (int i = 2; i >= 0; i--) {
                    floatFloatTreeMap.put(floats[i], xs[i]);
                }
                final int j = floatFloatTreeMap.firstEntry().getValue() == PanelScreen.x + PanelScreen.secondStart + 5 ? 0 : floatFloatTreeMap.firstEntry().getValue() == PanelScreen.x + PanelScreen.secondStart + 5 + offset + 5 ? 1 : 2;
                panelInner.x = floatFloatTreeMap.firstEntry().getValue();
                panelInner.y = floats[j];
                panelInner.scroll = scroll;
                floats[j] += panelInner.height + 5;
                panelInner.render(mouseX, mouseY);
                if (panelInner.y + panelInner.height > totalHeight) {
                    totalHeight = panelInner.y + panelInner.height;
                }
            }
            RenderUtil.releaseScissor();
            if (mouse < 0 && totalHeight + scroll > PanelScreen.y + PanelScreen.height - PanelScreen.secondEndY) {
                scroll -= 10;
            }
            if (mouse > 0 && scroll < 0) {
                scroll += 10;
            }
        }
    }

    protected boolean doesPanelInnerContain(final PanelInner panelInner, final String string) {
        return panelInner.panelSettings.stream().anyMatch(panelSetting -> panelSetting.getSetting().getName().toLowerCase().contains(string.toLowerCase()));
    }

    public void click(final int mouseX, final int mouseY, final int state) {
        if (PanelScreen.activeCategory != null && PanelScreen.activeCategory.equals(panelCategory)) {
            if (state == 0 && inside(mouseX, mouseY)) {
                PanelScreen.panelDrawable = null;
                PanelScreen.activeCategory = null;
                PanelScreen.activeModule = this;
            }
        }
        if (PanelScreen.activeModule != null && PanelScreen.activeModule.equals(this)) {
            panelInners.forEach(panelInner -> panelInner.click(mouseX, mouseY, state));
        }
    }

    public void release(final int mouseX, final int mouseY, final int state) {
        if (PanelScreen.activeModule != null && PanelScreen.activeModule.equals(this)) {
            panelInners.forEach(panelInner -> panelInner.release(mouseX, mouseY, state));
        }
    }

    public void type(final char typedChar, final int keyCode) {
        if (PanelScreen.activeModule != null && PanelScreen.activeModule.equals(this)) {
            panelInners.forEach(panelInner -> panelInner.type(typedChar, keyCode));
        }
    }

    protected boolean inside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    protected boolean doesPanelInnerListContainName(final String string) {
        return panelInners.stream().anyMatch(panelInner -> panelInner.name.equals(string));
    }

    protected float normalizeNumber(final float input, final float target, final float factor) {
        if (input > target) {
            return input - ((input - target) * factor) / 10;
        }
        if (input < target) {
            return input + ((target - input) * factor) / 10;
        }
        return input;
    }


    protected void hoverColor(final int mouseX, final int mouseY, final float animationFactor) {
        if (module.isEnabled()) {
            final Color color = PanelScreen.PANEL.color.GetColor();
            if (inside(mouseX, mouseY)) {
                col[0] = normalizeNumber(col[0], color.getRed() / (255.0f * 1.25f), animationFactor);
                col[1] = normalizeNumber(col[1], color.getGreen() / (255.0f * 1.25f), animationFactor);
                col[2] = normalizeNumber(col[2], color.getBlue() / (255.0f * 1.25f), animationFactor);

            } else {
                col[0] = normalizeNumber(col[0], color.getRed() / 255.0f, animationFactor);
                col[1] = normalizeNumber(col[1], color.getGreen() / 255.0f, animationFactor);
                col[2] = normalizeNumber(col[2], color.getBlue() / 255.0f, animationFactor);
            }
        } else {
            if (inside(mouseX, mouseY)) {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 0.6f, animationFactor));
            } else {
                IntStream.range(0, 3).forEach(i -> col[i] = normalizeNumber(col[i], 1.0f, animationFactor));
            }
        }
    }

}
