package sh.thered.gamingchair.client.mods;

//? if <=1.21.11 {
import net.minecraft.client.MinecraftClient;
//? } else {
/*import net.minecraft.client.Minecraft;
*///? }
import sh.thered.gamingchair.client.Mod;

public class BetterBoat extends Mod {
    static String name = "gc.betterboat";
    static String description = "Enables boat turning using your mouse rather than A/D keys.\n§6⚠ Warning: may be banned in boating tournaments.§r";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    //? if <=1.21.11 {
    static MinecraftClient mc = MinecraftClient.getInstance();
    //? } else {
    /*static Minecraft mc = Minecraft.getInstance();
    *///? }
    public static void cycle() {
        if(isDisabled(name)) return;

        if(mc.player == null) return;
        if(mc.world == null) return;
        if(mc.player.getVehicle() == null) return;
        if(mc.player.getVehicle().getControllingPassenger() != mc.player) return;

        mc.player.getVehicle().setYaw(mc.player.getYaw());
    }
}
