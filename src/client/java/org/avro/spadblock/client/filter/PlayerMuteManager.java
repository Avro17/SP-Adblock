package org.avro.spadblock.client.filter;

import org.avro.spadblock.client.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerMuteManager {
    private final ConfigManager configManager;

    public PlayerMuteManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean isPlayerMuted(@Nullable UUID playerUuid) {
        if (playerUuid == null) {
            return false;
        }
        return configManager.isPlayerMuted(playerUuid.toString());
    }

    public void mutePlayer(String uuid, String username) {
        configManager.addMutedPlayer(uuid, username);
    }

    public void unmutePlayer(String uuid) {
        configManager.removeMutedPlayer(uuid);
    }
}
