package me.tntz.gotify_integrator;

import me.tntz.gotify_integrator.tools.ConfigManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
