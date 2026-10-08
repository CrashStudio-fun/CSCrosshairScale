package fun.crashstudio.cscrosshairscale.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("CSCrosshairScale");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("cscrosshairscale.json");

    public static final float MIN_SCALE = 0.25f;
    public static final float MAX_SCALE = 4.0f;
    public static final float SCALE_STEP = 0.05f;
    public static final float DEFAULT_SCALE = 1.0f;

    public float scale = DEFAULT_SCALE;

    private static ModConfig instance;

    public static ModConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                instance = GSON.fromJson(reader, ModConfig.class);
                if (instance == null) {
                    instance = new ModConfig();
                    save();
                } else {
                    instance.validate();
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load CSCrosshairScale config, resetting to default", e);
                instance = new ModConfig();
                save();
            }
        } else {
            instance = new ModConfig();
            save();
        }
    }

    public static void save() {
        if (instance == null) {
            instance = new ModConfig();
        }
        try {
            Path parent = CONFIG_PATH.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save CSCrosshairScale config", e);
        }
    }

    public void setScale(float newScale) {
        float rounded = Math.round(newScale / SCALE_STEP) * SCALE_STEP;
        rounded = Math.round(rounded * 100.0f) / 100.0f;
        this.scale = Math.clamp(rounded, MIN_SCALE, MAX_SCALE);
    }

    public void changeScale(float delta) {
        setScale(this.scale + delta);
        save();
    }

    public void reset() {
        this.scale = DEFAULT_SCALE;
        save();
    }

    private void validate() {
        setScale(this.scale);
    }
}
