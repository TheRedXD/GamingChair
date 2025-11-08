package sh.thered.gamingchair.client.mods;

//? if 1.21.10 {
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
//?}
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.debug.DebugInfo;
import sh.thered.gamingchair.client.debug.DebugInfoCollectionBuilder;

import java.util.Collection;

public class BoatVelocity extends Mod {
    static String name = "gc.boatvelocity";
    static String description = "Displays boat velocity by drawing a line towards the current movement direction of the boat.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    static MinecraftClient mc = MinecraftClient.getInstance();

    //? if 1.21.10 {
    public static void cycle(WorldRenderContext worldRenderContext) {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("boatvelocity");
            return;
        }

        if(mc.player == null || mc.world == null || mc.player.getVehicle() == null || mc.player.getVehicle().getControllingPassenger() != mc.player) {
            Debugger.removeDebugInfoCollection("boatvelocity");
            return;
        }

        // Render a line towards where the boat has the most velocity

        // Get velocity
        double velocityX = mc.player.getVehicle().getVelocity().getX();
        double velocityY = mc.player.getVehicle().getVelocity().getY();
        double velocityZ = mc.player.getVehicle().getVelocity().getZ();

        // Normalize velocity
        double velocityMagnitude = Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        velocityX /= velocityMagnitude;
        velocityY /= velocityMagnitude;
        velocityZ /= velocityMagnitude;

        // Make sure to fix any NaNs
        if(Double.isNaN(velocityX)) velocityX = 0;
        if(Double.isNaN(velocityY)) velocityY = 0;
        if(Double.isNaN(velocityZ)) velocityZ = 0;

        Debugger.removeDebugInfoCollection("boatvelocity");

        Collection<DebugInfo> debugInfoCollection = new DebugInfoCollectionBuilder("boatvelocity")
            .add("velocityX", String.valueOf(velocityX), 0)
            .add("velocityY", String.valueOf(velocityY), 1)
            .add("velocityZ", String.valueOf(velocityZ), 2)
            .build();

        Debugger.appendDebugInfoCollection("boatvelocity", debugInfoCollection);

        // Render line

//        WorldRenderer worldRenderer = worldRenderContext.worldRenderer();
    }
    //?}

    public static void cycle() {
        if(isDisabled(name)) {
            Debugger.removeDebugInfoCollection("boatvelocity");
            return;
        }
    }
}
