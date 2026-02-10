package org.avro.spadblock.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import org.avro.spadblock.client.SPAdblockClient;
import org.avro.spadblock.util.UUIDResolver;

public class MuteCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("mute")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(MuteCommand::mutePlayer)));
    }

    private static int mutePlayer(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        String uuid = UUIDResolver.getUuidFromName(playerName);

        if (uuid != null) {
            SPAdblockClient.getConfigManager().addMutedPlayer(uuid, playerName);
            context.getSource().sendFeedback(Text.translatable("commands.spadblock.mute.success", playerName));
        } else {
            context.getSource().sendError(Text.translatable("commands.spadblock.mute.fail", playerName));
        }
        return 1;
    }
}
