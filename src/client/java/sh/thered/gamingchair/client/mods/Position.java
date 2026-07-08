package sh.thered.gamingchair.client.mods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.ModConfig;
import sh.thered.gamingchair.client.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position extends Mod {
    static String name = "gc.position";
    static String description = "A clean, text-only position HUD.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    public static List<String> texts;
    static Minecraft mc = Minecraft.getInstance();
    public static void cycle(GuiGraphicsExtractor drawContext, float v) {
        if (isDisabled(name)) return;
        if (!mc.getDebugOverlay().showDebugScreen() && !mc.options.hideGui) {
            int posy = 10;

            if (Mod.isEnabled("gc.hud")) posy = 45;

            List<String> activeStates = Mod.getActiveStates();
            activeStates.sort(String::compareTo);

            assert mc.player != null;
            int le_x = mc.player.blockPosition().getX();
            int le_y = mc.player.blockPosition().getY();
            int le_z = mc.player.blockPosition().getZ();
            float yaw = Utils.getYaw();
            String direction_nswe = Utils.getDirectionNSWE();
            String direction_XZ = Utils.getDirectionXZ();
            String text = "X: " + le_x + " Y: " + le_y + " Z: " + le_z + " | " + direction_nswe + " " + direction_XZ + " " + yaw;
            drawContext.text(mc.font, text, 10, posy, 0xffffffff, true);
        }
    }
}
