package org.avro.spadblock.client.filter;

import org.avro.spadblock.client.config.ConfigManager;
import java.util.Set;
import java.util.regex.Pattern;

public class ChatFilter {
    private final ConfigManager configManager;
    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    public ChatFilter(ConfigManager configManager) {
        this.configManager = configManager;
    }

    private String stripFormatting(String input) {
        if (input == null) return "";
        return STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
    }

    public boolean shouldShowMessage(String fullMessage) {
        String cleanMessage = stripFormatting(fullMessage).toLowerCase();

        int separatorIndex = cleanMessage.indexOf(':');

        String preColonPart = separatorIndex != -1 ? cleanMessage.substring(0, separatorIndex) : cleanMessage;

        Set<String> mutedNames = configManager.getMutedPlayerNames();
        for (String mutedName : mutedNames) {
            if (preColonPart.contains(mutedName.toLowerCase())) {
                return false;
            }
        }

        Set<String> activeWords = configManager.getActiveWords();
        for (String word : activeWords) {
            if (!word.trim().isEmpty() && cleanMessage.contains(word)) {
                return false;
            }
        }

        return true;
    }
}
