package org.avro.spadblock.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.avro.spadblock.client.SPAdblockClient;

import java.util.Collections;
import java.util.List;

public class AdblockCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("adblock")
                .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("word", StringArgumentType.greedyString())
                                .executes(AdblockCommand::addWord)))
                .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("word", StringArgumentType.greedyString())
                                .executes(AdblockCommand::removeWord)))
                .then(ClientCommandManager.literal("list")
                        .executes(AdblockCommand::listWords))
        );
    }

    private static int addWord(CommandContext<FabricClientCommandSource> context) {
        String word = StringArgumentType.getString(context, "word");
        SPAdblockClient.getConfigManager().addLocalWord(word);
        context.getSource().sendFeedback(Text.translatable("commands.spadblock.add.success", word));
        return 1;
    }

    private static int removeWord(CommandContext<FabricClientCommandSource> context) {
        String word = StringArgumentType.getString(context, "word");
        SPAdblockClient.getConfigManager().removeLocalWord(word);
        context.getSource().sendFeedback(Text.translatable("commands.spadblock.remove.success", word));
        return 1;
    }

    private static int listWords(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        List<String> words = SPAdblockClient.getConfigManager().getLocalWordsAsList();
        Collections.sort(words);

        if (words.isEmpty()) {
            source.sendFeedback(Text.translatable("commands.spadblock.list.empty"));
        } else {
            source.sendFeedback(Text.translatable("commands.spadblock.list.header"));
            for (String word : words) {
                source.sendFeedback(Text.literal("- ").append(word));
            }
            source.sendFeedback(Text.translatable("commands.spadblock.list.footer"));
        }
        return 1;
    }
}
