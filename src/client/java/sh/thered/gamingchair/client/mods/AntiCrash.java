package sh.thered.gamingchair.client.mods;

import sh.thered.gamingchair.client.Mod;

public class AntiCrash extends Mod {
    static String name = "gc.anticrash";
    static String description = "Prevents crash/lag exploits";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }
}
