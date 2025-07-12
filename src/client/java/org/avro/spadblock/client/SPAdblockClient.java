package org.avro.spadblock.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import org.avro.spadblock.SPAdblock;
import org.avro.spadblock.client.commands.*;
import org.avro.spadblock.client.config.ConfigManager;
import org.avro.spadblock.client.filter.ChatFilter;

public class SPAdblockClient implements ClientModInitializer {
    private static ConfigManager configManager;
    private static ChatFilter chatFilter;
    private static boolean easterEggShown = false;

    @Override
    public void onInitializeClient() {
        configManager = new ConfigManager();
        chatFilter = new ChatFilter(configManager);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            AdblockCommand.register(dispatcher);
            MuteCommand.register(dispatcher);
            UnmuteCommand.register(dispatcher);
            MuteListCommand.register(dispatcher);
        });

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) ->
                chatFilter.shouldShowMessage(message.getString()));

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) ->
                chatFilter.shouldShowMessage(message.getString()));

        registerEasterEggEvent();
        configManager.loadConfigs();
    }

    private void registerEasterEggEvent() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen && !easterEggShown) {
                SPAdblock.LOGGER.info("Avro x DocPaint <3");
                easterEggShown = true;
            }
        });
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
