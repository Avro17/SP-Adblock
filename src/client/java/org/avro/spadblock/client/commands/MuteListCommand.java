package org.avro.spadblock.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.avro.spadblock.client.SPAdblockClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MuteListCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("mutelist")
                .executes(MuteListCommand::listMutedPlayers));
    }

    private static int listMutedPlayers(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        Map<String, String> mutedPlayers = SPAdblockClient.getConfigManager().getMutedPlayers();

        if (mutedPlayers.isEmpty()) {
            source.sendFeedback(Text.translatable("commands.spadblock.mutelist.empty"));
        } else {
            source.sendFeedback(Text.translatable("commands.spadblock.mutelist.header"));
            List<String> playerNames = new ArrayList<>(mutedPlayers.values());
            Collections.sort(playerNames);
            for (String playerName : playerNames) {
                source.sendFeedback(Text.literal("- ").append(playerName));
            }
            source.sendFeedback(Text.translatable("commands.spadblock.mutelist.footer"));
        }
        return 1;
    }
}
