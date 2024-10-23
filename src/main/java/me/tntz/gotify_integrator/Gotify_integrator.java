package me.tntz.gotify_integrator;

import me.tntz.gotify_integrator.gotify.GotifyConnection;
import me.tntz.gotify_integrator.gotify.GotifyPriorityLevel;
import me.tntz.gotify_integrator.tools.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class Gotify_integrator implements ModInitializer {

    public static final String MOD_ID = "gotify_integrator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Gotify Integrator...");

        LOGGER.info("Loading config file...");
        try {
            ConfigManager.getConfig();
        } catch (IOException e) {
            LOGGER.error("Unexpected error: ", e);
            return;
        }

        LOGGER.info("Registering events...");
        EventRegisterer.onInitialize();

        LOGGER.info("Registering commands...");
        CommandsManager.onInitialize();

        LOGGER.info("Gotify Integrator has now been initialized.");
    }
}
