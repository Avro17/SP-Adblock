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
            if (player.getProfile().getName().equalsIgnoreCase(playerName)) {
                return player.getProfile().getId().toString();
            }
        }

        return null;
    }
}
