package org.avro.spadblock.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import org.avro.spadblock.SPAdblock;
import org.avro.spadblock.util.FileDownloader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ConfigManager {
    private static final String DEFAULT_CONFIG_1_URL = "https://raw.githubusercontent.com/Avro17/SP-Adblock/refs/heads/main/config1.json";
    private static final String DEFAULT_CONFIG_2_URL = "https://raw.githubusercontent.com/Avro17/SP-Adblock/refs/heads/main/config2.json";

    private static final String KEY_DEFAULT_CONFIG_1 = "config.spadblock.default1";
    private static final String KEY_DEFAULT_CONFIG_2 = "config.spadblock.default2";
    private static final String KEY_LOCAL_WORDS = "config.spadblock.local";
    private static final String CUSTOM_CONFIG_PREFIX = "config.spadblock.custom_prefix";

    private static final String LOCAL_CONFIG_FILE = "local_words.json";
    private static final String MUTED_PLAYERS_FILE = "muted_players.json";
    private static final String SETTINGS_FILE = "settings.json";

    private final Gson gson;
    private final File configDir;
    private final FileDownloader fileDownloader;
    private FilterConfig settings;
    private final Set<String> localWords;
    private final Map<String, String> mutedPlayers;
    private final Map<String, Boolean> configStates;
    private final Map<String, List<String>> configWords;

    public ConfigManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configDir = FabricLoader.getInstance().getConfigDir().resolve("spadblock").toFile();
        this.fileDownloader = new FileDownloader();
        this.localWords = new HashSet<>();
        this.mutedPlayers = new HashMap<>();
        this.configStates = new HashMap<>();
        this.configWords = new HashMap<>();
        ensureConfigDir();
        loadSettings();
    }

    private void ensureConfigDir() { if (!configDir.exists()) configDir.mkdirs(); }

    public void loadConfigs() {
        CompletableFuture.runAsync(() -> {
            SPAdblock.LOGGER.info("Starting to force-load all configs...");
            loadLocalWords();
            loadMutedPlayers();
            downloadAndLoadConfig(KEY_DEFAULT_CONFIG_1, DEFAULT_CONFIG_1_URL, "config1.json");
            downloadAndLoadConfig(KEY_DEFAULT_CONFIG_2, DEFAULT_CONFIG_2_URL, "config2.json");
            scanCustomConfigs();
            SPAdblock.LOGGER.info("Finished loading configs.");
        }).exceptionally(e -> {
            SPAdblock.LOGGER.error("An unexpected error occurred during config loading.", e);
            return null;
        });
    }

    private void downloadAndLoadConfig(String configNameKey, String url, String filename) {
        File configFile = new File(configDir, filename);
        try {
            fileDownloader.forceDownload(url, configFile);
        } catch (IOException e) {
            SPAdblock.LOGGER.error("Could not force download config '{}'. Error: {}", configNameKey, e.getMessage());
        }
        loadConfigFile(configNameKey, configFile);
    }

    private void loadSettings() {
        File settingsFile = new File(configDir, SETTINGS_FILE);
        if (settingsFile.exists()) {
            try (FileReader reader = new FileReader(settingsFile)) {
                settings = gson.fromJson(reader, FilterConfig.class);
            } catch (IOException e) { /* ... */ }
        }
        if (settings == null) settings = new FilterConfig();
        if (settings.configStates == null) settings.configStates = new HashMap<>();

        settings.configStates.putIfAbsent(KEY_DEFAULT_CONFIG_1, true);
        settings.configStates.putIfAbsent(KEY_DEFAULT_CONFIG_2, false);
        configStates.putAll(settings.configStates);
        saveSettings();
    }

    private void saveSettings() {
        try (FileWriter writer = new FileWriter(new File(configDir, SETTINGS_FILE))) {
            settings.configStates = this.configStates;
            gson.toJson(settings, writer);
        } catch (IOException e) { /* ... */ }
    }

    private void loadConfigFile(String configNameKey, File configFile) {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Type listType = new TypeToken<List<String>>(){}.getType();
                List<String> words = gson.fromJson(reader, listType);
                if (words != null) configWords.put(configNameKey, words);
            } catch (Exception e) { /* ... */ }
        }
        configStates.putIfAbsent(configNameKey, false);
    }

    private void scanCustomConfigs() {
        List<String> configsToRemove = new ArrayList<>();
        for (String configNameKey : new ArrayList<>(configStates.keySet())) {
            if (configNameKey.startsWith(CUSTOM_CONFIG_PREFIX)) {
                String fileName = configNameKey.substring(CUSTOM_CONFIG_PREFIX.length()) + ".json";
                File configFile = new File(configDir, fileName);
                if (!configFile.exists()) {
                    configsToRemove.add(configNameKey);
                }
            }
        }

        if (!configsToRemove.isEmpty()) {
            SPAdblock.LOGGER.info("Removing {} obsolete custom configs...", configsToRemove.size());
            for (String key : configsToRemove) {
                configStates.remove(key);
                configWords.remove(key);
            }
            saveSettings();
        }

        File[] jsonFiles = configDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json") &&
                        !name.equalsIgnoreCase(LOCAL_CONFIG_FILE) &&
                        !name.equalsIgnoreCase(MUTED_PLAYERS_FILE) &&
                        !name.equalsIgnoreCase(SETTINGS_FILE) &&
                        !name.equalsIgnoreCase("config1.json") &&
                        !name.equalsIgnoreCase("config2.json")
        );

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                String configNameKey = CUSTOM_CONFIG_PREFIX + jsonFile.getName().replace(".json", "");
                if (!configStates.containsKey(configNameKey)) {
                    SPAdblock.LOGGER.info("Found new custom config: {}", jsonFile.getName());
                    loadConfigFile(configNameKey, jsonFile);
                }
            }
        }
    }

    private void loadLocalWords() {
        File localFile = new File(configDir, LOCAL_CONFIG_FILE);
        loadConfigFile(KEY_LOCAL_WORDS, localFile);
        List<String> words = configWords.get(KEY_LOCAL_WORDS);
        if (words != null) {
            localWords.clear();
            localWords.addAll(words);
        }
        configStates.putIfAbsent(KEY_LOCAL_WORDS, true);
    }

    public void addLocalWord(String word) { if (localWords.add(word.toLowerCase())) saveLocalWords(); }
    public void removeLocalWord(String word) { if (localWords.remove(word.toLowerCase())) saveLocalWords(); }
    private void saveLocalWords() { try (FileWriter writer = new FileWriter(new File(configDir, LOCAL_CONFIG_FILE))) { gson.toJson(localWords, writer); configWords.put(KEY_LOCAL_WORDS, new ArrayList<>(localWords)); } catch (IOException e) { /* ... */ } }
    private void loadMutedPlayers() { File mutedFile = new File(configDir, MUTED_PLAYERS_FILE); if (mutedFile.exists()) { try (FileReader reader = new FileReader(mutedFile)) { Type mapType = new TypeToken<Map<String, String>>(){}.getType(); Map<String, String> players = gson.fromJson(reader, mapType); if (players != null) { mutedPlayers.clear(); mutedPlayers.putAll(players); } } catch (IOException e) { /* ... */ } } }
    private void saveMutedPlayers() { try (FileWriter writer = new FileWriter(new File(configDir, MUTED_PLAYERS_FILE))) { gson.toJson(mutedPlayers, writer); } catch (IOException e) { /* ... */ } }
    public void addMutedPlayer(String uuid, String username) { mutedPlayers.put(uuid, username); saveMutedPlayers(); }
    public void removeMutedPlayer(String uuid) { mutedPlayers.remove(uuid); saveMutedPlayers(); }
    public boolean isPlayerMuted(String uuid) { return mutedPlayers.containsKey(uuid); }
    public Set<String> getActiveWords() { Set<String> activeWords = new HashSet<>(); configStates.forEach((configName, isEnabled) -> { if (Boolean.TRUE.equals(isEnabled)) { List<String> words = configWords.get(configName); if (words != null) words.forEach(word -> activeWords.add(word.toLowerCase())); } }); return activeWords; }
    public Map<String, Boolean> getConfigStates() { return Collections.unmodifiableMap(configStates); }
    public void setConfigEnabled(String configName, boolean enabled) { configStates.put(configName, enabled); saveSettings(); }
    public List<String> getLocalWordsAsList() { return new ArrayList<>(localWords); }
    public Map<String, String> getMutedPlayers() { return Collections.unmodifiableMap(mutedPlayers); }
    public Set<String> getMutedPlayerNames() { return new HashSet<>(this.mutedPlayers.values()); }
}
