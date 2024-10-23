package me.tntz.gotify_integrator;

import me.tntz.gotify_integrator.gotify.GotifyConnection;
import me.tntz.gotify_integrator.gotify.GotifyPriorityLevel;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;



public class EventRegisterer {
    public static void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(EventRegisterer::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPED.register(EventRegisterer::onServerStop);

        ServerMessageEvents.GAME_MESSAGE.register(EventRegisterer::onGameMessage);
        ServerMessageEvents.CHAT_MESSAGE.register(EventRegisterer::onChatMessage);

        ServerPlayConnectionEvents.JOIN.register(EventRegisterer::onPlayerJoin);
        ServerPlayConnectionEvents.DISCONNECT.register(EventRegisterer::onPlayerDisconnect);
    }

    private static void onServerStart(MinecraftServer minecraftServer) {
        try {
            GotifyConnection.sendMessage("SERVERSON", "Server started!", GotifyPriorityLevel.HIGH);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void onServerStop(MinecraftServer minecraftServer) {
        try {
            GotifyConnection.sendMessage("SERVERSOFF", "Server stopped!", GotifyPriorityLevel.HIGH);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private static void onGameMessage(MinecraftServer server, Text text, boolean bool) {
        String rawText = text.getString();
        if (rawText.matches("^.+ (joined|left) the game$")) return;

        try {
            GotifyConnection.sendMessage(
                    text.getString(),
                    "Server message.",
                    GotifyPriorityLevel.NORMAL
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void onChatMessage(SignedMessage signedMessage, ServerPlayerEntity serverPlayerEntity, MessageType.Parameters parameters) {
        try {
            GotifyConnection.sendMessage(
                    serverPlayerEntity.getName().getString() + " messaged in the server",
                    signedMessage.getSignedContent(),
                    GotifyPriorityLevel.LOW
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private static void sendPlayerFlowNotif(ServerPlayNetworkHandler handler, MinecraftServer server, String titleAction, String descriptionAction, int cpcOffset) {
        String username = handler.getPlayer().getName().getString();
        try {
            GotifyConnection.sendMessage(
                    username + " " + titleAction + "! [" + (server.getCurrentPlayerCount() + cpcOffset) + "/" + server.getMaxPlayerCount() + "]",
                    username + " just " + descriptionAction + " the server.",
                    GotifyPriorityLevel.HIGH
            );
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        sendPlayerFlowNotif(handler, server, "joined", "joined", 1);
    }

    private static void onPlayerDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        sendPlayerFlowNotif(handler, server, "disconnected", "disconnected from", -1);
    }
}
