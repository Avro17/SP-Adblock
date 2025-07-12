package org.avro.spadblock.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.avro.spadblock.client.config.ConfigManager;

import java.util.*;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final ConfigManager configManager;
    private final Map<String, CheckboxWidget> configCheckboxes = new LinkedHashMap<>();

    public ConfigScreen(Screen parent, ConfigManager configManager) {
        super(Text.translatable("gui.spadblock.config.title"));
        this.parent = parent;
        this.configManager = configManager;
    }

    @Override
    protected void init() {
        super.init();
        this.configCheckboxes.clear();
        int y = 40;

        Map<String, Boolean> configStates = configManager.getConfigStates();
        List<String> sortedConfigNames = new ArrayList<>(configStates.keySet());
        Collections.sort(sortedConfigNames);

        for (String configNameKey : sortedConfigNames) {
            Text displayText;
            if (configNameKey.startsWith("config.spadblock.custom_prefix")) {
                String fileName = configNameKey.substring("config.spadblock.custom_prefix".length());
                displayText = Text.translatable("config.spadblock.custom_prefix").append(fileName);
            } else {
                displayText = Text.translatable(configNameKey);
            }

            CheckboxWidget checkbox = CheckboxWidget.builder(displayText, this.textRenderer)
                    .checked(configStates.getOrDefault(configNameKey, false))
                    .pos(width / 2 - 100, y)
                    .build();

            this.configCheckboxes.put(configNameKey, checkbox);
            addDrawableChild(checkbox);
            y += 25;
        }

        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"),
                button -> close()).dimensions(width / 2 - 100, height - 30, 200, 20).build());
    }

    @Override
    public void close() {
        for (Map.Entry<String, CheckboxWidget> entry : this.configCheckboxes.entrySet()) {
            configManager.setConfigEnabled(entry.getKey(), entry.getValue().isChecked());
        }
        if (this.client != null) this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
    }
}
