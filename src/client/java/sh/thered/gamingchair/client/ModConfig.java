package sh.thered.gamingchair.client;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    static Map<String, Map<String, Object>> options = new HashMap<>();

    public static void setOption(String modName, String option, Object value) {
        if(!options.containsKey(modName)) {
            options.put(modName, new HashMap<>());
        }
        Map<String, Object> thisOptions = options.get(modName);
        thisOptions.put(option, value);
    }

    public static void setOptions(String modName, Map<String, Object> thisOptions) {
        options.put(modName, thisOptions);
    }

    @Nullable
    public static Object getOption(String modName, String option) {
        if(!options.containsKey(modName) || !options.get(modName).containsKey(option)) {
            return null;
        }

        return options.get(modName).get(option);
    }

    @Nullable
    public static Map<String, Object> getOptions(String modName) {
        if(!options.containsKey(modName)) return null;
        return options.get(modName);
    }

    public static void dropOptions(String modName) {
        options.remove(modName);
    }

    public static void dropOption(String modName, String option) {
        if(!options.containsKey(modName)) return;
        options.get(modName).remove(option);
    }
}
