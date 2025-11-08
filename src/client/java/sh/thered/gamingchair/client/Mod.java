package sh.thered.gamingchair.client;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import sh.thered.gamingchair.client.mods.ActiveMods;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Mod {
    static Map<String, Boolean> states = new HashMap<>();
    static Map<String, ModConfig> configs = new HashMap<>();
    static Map<String, Mod> mods = new HashMap<>();

    public static boolean isDisabled(String modName) {
        if(!states.containsKey(modName)) return true;
        boolean state = states.get(modName);
        return !state;
    }
    public static boolean isEnabled(String modName) {
        if(!states.containsKey(modName)) return true;
        return states.get(modName);
    }

    public static void setState(String modName, boolean state) {
        setStateInternal(modName, state);
        ActiveMods.update();
//        Mod.exportEnabledMods();
    }

    public static void setStateInternal(String modName, boolean state) {
        ActiveMods.update();
        states.put(modName, state);
        configs.get(modName).setState(state);
    }

    @Nullable
    public static Boolean getState(String modName) {
        if(!states.containsKey(modName)) return null;
        return states.get(modName);
    }

    public static Boolean existsState(String modName) {
        return states.containsKey(modName);
    }

    public static List<String> getActiveStates() {
        List<String> stringList = new ArrayList<>(List.of());
        for(Map.Entry<String, Boolean> entry : states.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if(value) {
                stringList.add(key);
            }
        }
        return stringList;
    }

    public static List<String> getStates() {
        List<String> stringList = new ArrayList<>(List.of());
        for(Map.Entry<String, Boolean> entry : states.entrySet()) {
            String key = entry.getKey();
            stringList.add(key);
        }
        return stringList;
    }

    public static void dropState(String modName) {
        states.remove(modName);
    }

    public static void setConfig(String modName, ModConfig modConfig) {
        configs.put(modName, modConfig);
    }

    @Nullable
    public static ModConfig getConfig(String modName) {
        if(!configs.containsKey(modName)) return null;
        return configs.get(modName);
    }

    public static void dropConfig(String modName) {
        configs.remove(modName);
    }

    public String getName() {
        return "";
    }

    public String getDescription() {
        return "No description provided.";
    }

    public static void setupMods(List<Map<Mod, ModConfig>> entries) {
        entries.forEach(entry -> {
            entry.forEach((mod, modConfig) -> {
                states.put(mod.getName(), modConfig.getState());
                configs.put(mod.getName(), modConfig);
                mods.put(mod.getName(), mod);
            });
        });
        ActiveMods.update();
    }

    public static Mod getMod(String modName) {
        return mods.get(modName);
    }

    public static void exportEnabledMods() {
        Path configDir = FabricLoader.getInstance().getConfigDir();

        Path gamingchairDir = configDir.resolve("gamingchair");
        if (!java.nio.file.Files.exists(gamingchairDir)) {
            try {
                java.nio.file.Files.createDirectories(gamingchairDir);
            } catch (java.io.IOException e) {
                System.err.println("Failed to create GamingChair config directory: " + e.getMessage());
            }
        }

        Path enabledModsFile = gamingchairDir.resolve("enabled_mods.txt");
        if (!java.nio.file.Files.exists(enabledModsFile)) {
            try {
                java.nio.file.Files.createFile(enabledModsFile);
            } catch (java.io.IOException e) {
                System.err.println("Failed to create enabled_mods.txt file: " + e.getMessage());
            }
        }

        if (shouldUpdateEnabledMods()) {
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(enabledModsFile.toFile()))) {
                for (String modName : getActiveStates()) {
                    writer.write(modName);
                    writer.newLine();
                }
            } catch (java.io.IOException e) {
                System.err.println("Failed to write enabled mods to file: " + e.getMessage());
            }
        }
    }

    public static void pullFromEnabledMods() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path gamingchairDir = configDir.resolve("gamingchair");

        Path enabledModsFile = gamingchairDir.resolve("enabled_mods.txt");

        if (java.nio.file.Files.exists(enabledModsFile)) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(enabledModsFile.toFile()))) {
                List<String> setMods = new ArrayList<>();
                String line;
                List<String> currentStates = Mod.getStates();
                while ((line = reader.readLine()) != null) {
                    if (Mod.existsState(line)) {
                        Mod.setStateInternal(line, true);
                        setMods.add(line);
                    }
                }
                for (String mod : currentStates) {
                    if (!setMods.contains(mod)) {
                        Mod.setStateInternal(mod, false);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Mod.exportEnabledMods();
        }

        ActiveMods.update();
    }

    public static List<String> pullListFromEnabledMods() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path gamingchairDir = configDir.resolve("gamingchair");

        Path enabledModsFile = gamingchairDir.resolve("enabled_mods.txt");

        List<String> toReturn = new ArrayList<>();

        if (java.nio.file.Files.exists(enabledModsFile)) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(enabledModsFile.toFile()))) {
                List<String> setMods = new ArrayList<>();
                String line;
                List<String> currentStates = Mod.getStates();
                while ((line = reader.readLine()) != null) {
                    if (Mod.existsState(line)) {
                        setMods.add(line);
                    }
                }
                toReturn = setMods;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return toReturn;
    }

    public static boolean shouldUpdateEnabledMods() {
        List<String> pulledMods = pullListFromEnabledMods();
        List<String> activeMods = getActiveStates();

        if (pulledMods.size() != activeMods.size()) {
            return true;
        }

        Set<String> pulledSet = new HashSet<>(pulledMods);
        Set<String> activeSet = new HashSet<>(activeMods);

        return !pulledSet.equals(activeSet);
    }
}
