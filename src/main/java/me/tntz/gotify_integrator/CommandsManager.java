package me.tntz.gotify_integrator;

import com.google.common.collect.Lists;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.tntz.gotify_integrator.gotify.GotifyPriorityLevel;
import me.tntz.gotify_integrator.gotify.GotifyPriorityLevelManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;


public class CommandsManager {
    public static void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            LiteralArgumentBuilder<ServerCommandSource> root = literal("setminprioritylevel").requires(source -> source.hasPermissionLevel(2));
            List<LiteralArgumentBuilder<ServerCommandSource>> commands = Lists.transform(
                    Arrays.asList(GotifyPriorityLevel.values()),
                    priorityLevel ->
                        root.then(
                                literal(priorityLevel.name()).executes(
                                        ctx -> setMinPriorityLevel(priorityLevel, ctx)
                                )
                        )
            );

            for (LiteralArgumentBuilder<ServerCommandSource> command : commands) {
                commandDispatcher.register(command);
            }
        }));
    }

    public static int setMinPriorityLevel(GotifyPriorityLevel priorityLevel, CommandContext<ServerCommandSource> ctx) {
        GotifyPriorityLevelManager.setMinPriorityLevel(priorityLevel);
        ctx.getSource().sendFeedback(() -> Text.literal("Set minimum priority of Gotify notifications to " + priorityLevel.name() + "."), true);
        return 0;
    }
}
