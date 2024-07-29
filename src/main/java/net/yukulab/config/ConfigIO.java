package net.yukulab.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.yukulab.TakeItPairs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class ConfigIO {
    private static final String configFolderName = TakeItPairs.MOD_ID;
    private static final Gson gson = new Gson();

    public static <T> String getConfigFileSuffix(@NotNull Class<T> configClass) {
        if (configClass.equals(ClientConfig.class)) return "client";
        return "unknown";
    }

    private static File getRootConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toFile();
    }

    private static File getConfigDir() {
        File configDir = new File(getRootConfigDir(), configFolderName);
        if (!configDir.exists()) configDir.mkdir();
        return configDir;
    }

    private static <T> File getConfigFile(@NotNull Class<T> configClass) {
        return new File(getConfigDir(), getConfigFileSuffix(configClass) + ".json");
    }

    // Read & Write
    public static <T> void writeConfig(@NotNull T config) {
        writeConfig(getConfigFile(config.getClass()), config);
    }

    @VisibleForTesting
    public static <T> void writeConfig(@NotNull File configFile, @NotNull T config) {
        try (var writer = new FileWriter(configFile, false)) {
            writer.write(gson.toJson(config));
            TakeItPairs.LOGGER.debug("{} was wrote.", configFile.getName());
        } catch (IOException e) {
            TakeItPairs.LOGGER.error("Failed to write config", e);
        }
    }

    public static <T> T readConfigOrDefault(@NotNull Class<T> configClass, T defaultValue) {
        Optional<T> readConfig = readConfig(configClass);
        if (readConfig.isEmpty()) TakeItPairs.LOGGER.warn("Config file wasn't found. Fallbacks default value.");
        return readConfig.orElse(defaultValue);
    }

    public static <T> Optional<T> readConfig(@NotNull Class<T> configClass) {
        return readConfig(getConfigFile(configClass), configClass);
    }

    @VisibleForTesting
    public static <T> Optional<T> readConfig(@NotNull File configFile, @NotNull Class<T> configClass) {
        if (!configFile.exists()) return Optional.empty();
        try (var reader = new FileReader(configFile)) {
            return Optional.of(gson.fromJson(reader, configClass));
        } catch (IOException e) {
            TakeItPairs.LOGGER.error("Failed to read json", e);
        }
        return Optional.empty();
    }
}
