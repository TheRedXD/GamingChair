package sh.thered.gamingchair.client.mods;

import net.minecraft.client.MinecraftClient;
import sh.thered.gamingchair.client.Mod;

public class BetterBoat extends Mod {
    static String name = "gc.betterboat";
    static String description = "Enables boat turning using your mouse rather than A/D keys.\n§6⚠ Warning: may be banned in boating tournaments.§r";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    static MinecraftClient mc = MinecraftClient.getInstance();
    public static void cycle() {
        if(isDisabled(name)) return;

        if(mc.player == null) return;
        if(mc.world == null) return;
        if(mc.player.getVehicle() == null) return;
        if(mc.player.getVehicle().getControllingPassenger() != mc.player) return;

        mc.player.getVehicle().setYaw(mc.player.getYaw());
    }
}
