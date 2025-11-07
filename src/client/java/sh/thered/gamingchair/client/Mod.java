package sh.thered.gamingchair.client;

import org.jetbrains.annotations.Nullable;
import sh.thered.gamingchair.client.mods.ActiveMods;

import java.util.*;

public class Mod {
    static Map<String, Boolean> states = new HashMap<>();
    static Map<String, ModConfig> configs = new HashMap<>();
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
        ActiveMods.update();
        states.put(modName, state);
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
}
