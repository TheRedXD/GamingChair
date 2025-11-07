package sh.thered.gamingchair.client.mods;

import net.minecraft.client.gui.DrawContext;
import sh.thered.gamingchair.client.debug.DebugInfo;
import sh.thered.gamingchair.client.Mod;

import java.util.*;

public class Debugger extends Mod {
    static String name = "gc.debugger";

    static Map<String, String> debugInfo = new HashMap<>();
    static Map<String, Integer> debugInfoPriority = new HashMap<>();

    static Map<String, Collection<DebugInfo>> debugInfoCollections = new HashMap<>();

    public static void render(DrawContext drawContext, float v) {
        if(isDisabled(name)) return;
        int padding = 3;
        int x = 10;
        int y = 60;

        for (int i = 0; i < debugInfo.size(); i++) {
            for (Map.Entry<String, String> entry : debugInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (debugInfoPriority.get(key) == i) {
                    drawContext.drawText(Hud.mc.textRenderer, key + ": " + value, x, y, 0xFFFFFFFF, false);
                    y += Hud.mc.textRenderer.getWrappedLinesHeight(key + ": " + value, Hud.mc.textRenderer.getWidth(key + ": " + value)) + padding;
                }
            }
        }
    }

    public static void set(String key, String value, int priority) {
        if (Objects.equals(debugInfo.get(key), value) && Objects.equals(debugInfoPriority.get(key), priority)) return;
        debugInfo.put(key, value);
        debugInfoPriority.put(key, priority);
    }

    public static int getLastPriority() {
        Collection<Integer> values = debugInfoPriority.values();
        final int[] b = {0};
        values.iterator().forEachRemaining(a -> {
            if (a > b[0]) b[0] = a;
        });
        return b[0];
    }

    public static void appendDebugInfoCollection(String name, Collection<DebugInfo> debugInfoCollection) {
        debugInfoCollections.put(name, debugInfoCollection);
        int lastPriority = getLastPriority();
        for (DebugInfo debugInfo : debugInfoCollection) {
            set(debugInfo.getName(), debugInfo.getValue(), lastPriority + debugInfo.getPriority());
        }
    }

    public static void removeDebugInfoCollection(String name) {
        if (!debugInfoCollections.containsKey(name)) return;
        Collection<DebugInfo> debugInfoCollection = debugInfoCollections.get(name);
        for (DebugInfo debugInfo : debugInfoCollection) {
            delete(debugInfo.getName());
        }
        debugInfoCollections.remove(name);
    }

    public static void delete(String key) {
        debugInfo.remove(key);
        debugInfoPriority.remove(key);
    }

    public static void cycle(DrawContext drawContext, float v) {
        if(isDisabled(name)) return;
        if (!Hud.mc.getDebugHud().shouldShowDebugHud() && !Hud.mc.options.hudHidden) {
            render(drawContext, v);
        }
    }
}
