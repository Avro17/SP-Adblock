package org.avro.spadblock.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.Collection;

public class UUIDResolver {
    public static String getUuidFromName(String playerName) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            return null;
        }

        Collection<PlayerListEntry> players = client.getNetworkHandler().getPlayerList();
        for (PlayerListEntry player : players) {
            // В 1.21.11 используйте name() вместо getName() и id() вместо getId()
            if (player.getProfile().name().equalsIgnoreCase(playerName)) {
                return player.getProfile().id().toString();
            }
        }

        return null;
    }
}
