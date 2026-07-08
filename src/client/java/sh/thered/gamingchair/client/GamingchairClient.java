package sh.thered.gamingchair.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
            Map.of(new Bentifier(), new ModConfig(false)),
            Map.of(new AntiCrash(), new ModConfig(true))
        ));

        Mod.pullFromEnabledMods();

        HudElementRegistry.addLast(Identifier.parse("sh.thered.gamingchair"), (context, tickCounter) -> {
            Hud.cycle(context, tickCounter.getRealtimeDeltaTicks());
            ActiveMods.cycle(context, tickCounter.getRealtimeDeltaTicks());
            Debugger.cycle(context, tickCounter.getRealtimeDeltaTicks());
            Position.cycle(context, tickCounter.getRealtimeDeltaTicks());
            ModMenu.cycle();
        });

        LevelRenderEvents.END_MAIN.register(context -> {
            PoseStack poseStack = context.poseStack();

            BlockHighlighter.cycle(poseStack);
            BoatVelocity.cycle(poseStack);
        });

        ClientSendMessageEvents.MODIFY_CHAT.register(Identifier.parse("sh.thered.gamingchair"), (message) -> {
            message = Uwuifier.chatPass(message);
            message = Bentifier.chatPass(message);
            return message;
        });

        try {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    BetterBoat.cycle();
                    Debugger.set("fps", String.valueOf(Minecraft.getInstance().getFps()), 0);
                    Debugger.set("frametime", String.valueOf(Minecraft.getInstance().getFrameTimeNs()/1000000d)+"ms", 1);
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
                                        source.sendFeedback(Component.literal("§8[§dGamingChair§8] §d" + arg + " toggled §coff§d!"));
                                    } else {
                                        Mod.setState(arg, !Boolean.TRUE.equals(Mod.getState(arg)));
                                        source.sendFeedback(Component.literal("§8[§dGamingChair§8] §d" + arg + " toggled §aon§d!"));
                                    }
                                    Mod.exportEnabledMods();
                                } else {
                                    source.sendFeedback(Component.literal("§8[§dGamingChair§8] §d" + arg + " is not a defined Gaming Chair mod."));
                                }
                                return 1;
                            })
                            .suggests((context, builder) -> {
                                for (String mod : Mod.getStates()) {
                                    builder.suggest(mod, Component.literal(Mod.getMod(mod).getDescription()));
                                }
                                return builder.buildFuture();
                            })
                    )
                    .executes(context -> {
                        FabricClientCommandSource source = (FabricClientCommandSource) context.getSource();
                        source.sendFeedback(Component.literal("§8[§dGamingChair§8] §d/gamingchair requires 1 argument!"));
                        return 1;
                    });
            dispatcher.register(
                    (LiteralArgumentBuilder<FabricClientCommandSource>) (Object) argbuilder
            );
        });

        System.out.println("amogus");
    }
}
