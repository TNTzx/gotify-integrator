package me.tntz.gotify_integrator.tools;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfigManager {
    public static final Path CONFIG_FOLDER = Path.of("config");
    public static final Path CONFIG_NAME = Path.of("gotify_integrator.toml");
    public static final Path CONFIG_LOCATION = Paths.get(CONFIG_FOLDER.toString(), CONFIG_NAME.toString());

    private static Config cachedConfig = null;


    private static String getToml() throws IOException {
        Files.createDirectories(CONFIG_FOLDER);

        File file = CONFIG_LOCATION.toFile();
        if (file.createNewFile()) {
            Config defaultConfig = new Config();

            TomlWriter tomlWriter = new TomlWriter();
            StringWriter stringWriter = new StringWriter();
            tomlWriter.write(defaultConfig.getData(), stringWriter);
            String parsed = stringWriter.toString();

            try (FileWriter myWriter = new FileWriter(file)) {
                myWriter.write(parsed);
            }
        }

        String result;
        try (Scanner myReader = new Scanner(file).useDelimiter("\\Z")) {
            result = myReader.next();
        }

        return result;
    }

    public static Config readConfig() throws IOException {
        String tomlString = getToml();
        return new Toml().read(tomlString).to(Config.class);
    }

    public static Config reloadConfig() throws IOException {
        cachedConfig = readConfig();
        return cachedConfig;
    }

    public static Config getConfig() throws IOException {
        if (cachedConfig == null) return reloadConfig();
        return cachedConfig;
    }
}
