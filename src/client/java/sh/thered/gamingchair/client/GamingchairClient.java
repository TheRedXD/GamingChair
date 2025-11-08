package sh.thered.gamingchair.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//? if 1.21.10 {
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
//?}
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sh.thered.gamingchair.client.mods.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class GamingchairClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Mod.setupMods(List.of(
            Map.of(new BetterBoat(), new ModConfig(true)),
            Map.of(new BlockHighlighter(), new ModConfig(false)),
            Map.of(new Debugger(), new ModConfig(false)),
            Map.of(new Hud(), new ModConfig(true)),
            Map.of(new ActiveMods(), new ModConfig(true)),
            Map.of(new Position(), new ModConfig(true)),
            Map.of(new BoatVelocity(), new ModConfig(false)),
            Map.of(new Uwuifier(), new ModConfig(false)),
            Map.of(new Bentifier(), new ModConfig(false))
        ));

        Mod.pullFromEnabledMods();

        HudElementRegistry.addLast(Identifier.of("sh.thered.gamingchair"), (context, tickCounter) -> {
            Hud.cycle(context, tickCounter.getDynamicDeltaTicks());
            ActiveMods.cycle(context, tickCounter.getDynamicDeltaTicks());
            Debugger.cycle(context, tickCounter.getDynamicDeltaTicks());
            Position.cycle(context, tickCounter.getDynamicDeltaTicks());
            ModMenu.cycle();
        });

        //? if 1.21.10 {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(Identifier.of("sh.thered.gamingchair"), BlockHighlighter::cycle);
        WorldRenderEvents.END_MAIN.register(Identifier.of("sh.thered.gamingchair"), BoatVelocity::cycle);
        //?}

        ClientSendMessageEvents.MODIFY_CHAT.register(Identifier.of("sh.thered.gamingchair"), (message) -> {
            message = Uwuifier.chatPass(message);
            message = Bentifier.chatPass(message);
            return message;
        });

        try {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    BetterBoat.cycle();
                    Debugger.set("fps", String.valueOf(MinecraftClient.getInstance().getCurrentFps()), 0);
                    Debugger.set("frametime", String.valueOf(MinecraftClient.getInstance().getRenderTime()/1000000d)+"ms", 1);
                }
            }, 0, 1000/120);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Requested element not found");
        }

        // Commands

        // WARN: Should fix, contains unchecked cast
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralArgumentBuilder<Object> argbuilder = LiteralArgumentBuilder.literal("gamingchair")
                    .then(argument("modName", StringArgumentType.string())
                            .executes(context -> {
                                FabricClientCommandSource source = (FabricClientCommandSource) context.getSource();
                                final String arg = StringArgumentType.getString(context, "modName");
                                if (Mod.existsState(arg)) {
                                    if (Boolean.TRUE.equals(Mod.getState(arg))) {
                                        Mod.setState(arg, !Boolean.TRUE.equals(Mod.getState(arg)));
                                        source.sendFeedback(Text.literal("§8[§dGamingChair§8] §d" + arg + " toggled §coff§d!"));
                                    } else {
                                        Mod.setState(arg, !Boolean.TRUE.equals(Mod.getState(arg)));
                                        source.sendFeedback(Text.literal("§8[§dGamingChair§8] §d" + arg + " toggled §aon§d!"));
                                    }
                                    Mod.exportEnabledMods();
                                } else {
                                    source.sendFeedback(Text.literal("§8[§dGamingChair§8] §d" + arg + " is not a defined Gaming Chair mod."));
                                }
                                return 1;
                            })
                            .suggests((context, builder) -> {
                                for (String mod : Mod.getStates()) {
                                    builder.suggest(mod, Text.of(Mod.getMod(mod).getDescription()));
                                }
                                return builder.buildFuture();
                            })
                    )
                    .executes(context -> {
                        FabricClientCommandSource source = (FabricClientCommandSource) context.getSource();
                        source.sendFeedback(Text.literal("§8[§dGamingChair§8] §d/gamingchair requires 1 argument!"));
                        return 1;
                    });
            dispatcher.register(
                (LiteralArgumentBuilder<FabricClientCommandSource>) (Object) argbuilder
            );
        });

        System.out.println("amogus");
    }
}
