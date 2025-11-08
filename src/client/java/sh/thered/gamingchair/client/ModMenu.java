package sh.thered.gamingchair.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import sh.thered.gamingchair.client.mods.Bentifier;
import sh.thered.gamingchair.client.mods.Debugger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModMenu {
    public static ModMenuScreen modMenuScreen = new ModMenuScreen();
    static MinecraftClient mc = MinecraftClient.getInstance();
    static boolean triggerToggle = false;

    public static class ModMenuButton extends ButtonWidget {
        public static final int OUTLINE_THICKNESS = 1;
        public static final int BG_COLOR = 0x22FFFFFF; // ARGB: semi-transparent white
        public static final int OUTLINE_COLOR = 0xFFFFFFFF; // ARGB: solid white

        public String buttonModName = "";

        protected ModMenuButton(int x, int y, int width, int height, Text message, PressAction onPress, Text message1, PressAction onPress1, String modName) {
            super(x, y, width, height, message, onPress, new NarrationSupplier() {
                @Override
                public MutableText createNarrationMessage(Supplier<MutableText> textSupplier) {
                    return textSupplier.get();
                }
            });

            buttonModName = modName;
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (buttonModName.endsWith(".settings")) {
                if (buttonModName.substring(0, buttonModName.lastIndexOf(".")).equals(ModMenuScreen.settingsForMod) && ModMenuScreen.settingsOpen) {
                    int rainbowInt = Utils.getRainbowInt();
                    // TODO: make the transparency make sense
                    int transparentRainbowInt = Utils.getRainbowInt() - 0xff000000 | 0x22000000;

                    // Draw transparent rainbow background
                    context.fill(getX(), getY(), getX() + width, getY() + height, transparentRainbowInt);

                    // Draw rainbow outline
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, rainbowInt); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, rainbowInt); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, rainbowInt); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, rainbowInt); // Right
                } else {
                    // Draw transparent white background
                    context.fill(getX(), getY(), getX() + width, getY() + height, BG_COLOR);

                    // Draw white outline
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, OUTLINE_COLOR); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, OUTLINE_COLOR); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, OUTLINE_COLOR); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, OUTLINE_COLOR); // Right
                }
            } else {
                if (Mod.isEnabled(buttonModName)) {
                    int rainbowInt = Utils.getRainbowInt();
                    // TODO: make the transparency make sense
                    int transparentRainbowInt = Utils.getRainbowInt() - 0xff000000 | 0x22000000;

                    // Draw transparent rainbow background
                    context.fill(getX(), getY(), getX() + width, getY() + height, transparentRainbowInt);

                    // Draw rainbow outline
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, rainbowInt); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, rainbowInt); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, rainbowInt); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, rainbowInt); // Right
                } else if (Mod.isDisabled(buttonModName)) {
                    // Draw transparent white background
                    context.fill(getX(), getY(), getX() + width, getY() + height, BG_COLOR);

                    // Draw white outline
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, OUTLINE_COLOR); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, OUTLINE_COLOR); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, OUTLINE_COLOR); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, OUTLINE_COLOR); // Right
                }
            }

            // Draw text
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, getMessage(), getX() + width / 2, getY() + (height - 8) / 2, 0xFFFFFFFF);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ModMenuScreen extends Screen {

        protected ModMenuScreen() {
            super(Text.literal("ModMenu"));
        }

        public ButtonWidget button1;
        public ButtonWidget button2;

        public static String settingsForMod = "";
        public static boolean settingsOpen = false;
        public static int settingsWidth = 200;
        public static int settingsXOffset = settingsWidth;
        public static double settingsOpenTime = 0;
        public static double settingsCloseTime = 0;

        @Override
        protected void init() {
//            button1 = ButtonWidget.builder(Text.literal("Button 1"), button -> {
//                System.out.println("You clicked button1!");
//            })
//                .dimensions(width / 2 - 205, 20, 200, 20)
//                .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
//                .build();
//            button2 = ButtonWidget.builder(Text.literal("Button 2"), button -> {
//                System.out.println("You clicked button2!");
//            })
//                .dimensions(width / 2 + 5, 20, 200, 20)
//                .tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
//                .build();

            // Mod toggle buttons
            int startingYValue = 40;
            int yValue = startingYValue;
            String[] sortedStates = Mod.getStates().stream().sorted(Comparator.naturalOrder()).toArray(String[]::new);
            for (String mod : sortedStates) {
                ModMenuButton bagel = new ModMenuButton(20, yValue, 100, 16, Text.literal(mod), button -> {
                    Mod.setState(mod, !Mod.isEnabled(mod));
                    button.setTooltip(Tooltip.of(Text.literal(Mod.getMod(mod).getDescription() + "\n\n§oCurrently " + (Mod.isEnabled(mod) ? "§aenabled" : "§cdisabled") + ".")));
                }, null, null, mod);
                bagel.setTooltip(Tooltip.of(Text.literal(Mod.getMod(mod).getDescription() + "\n\n§oCurrently " + (Mod.isEnabled(mod) ? "§aenabled" : "§cdisabled") + ".")));
                addDrawableChild(bagel);
                yValue += 2 + 16;
            }
            yValue = startingYValue;
            for (String mod : sortedStates) {
                ModMenuButton bagel = new ModMenuButton(20 + 100 + 2, yValue, 16, 16, Text.literal(">"), button -> {
                    if (settingsForMod.equals(mod)) {
                        settingsOpen = !settingsOpen;
                        settingsForMod = mod;
                        modMenuScreen.settingsXOffset = modMenuScreen.settingsWidth;
                    } else {
                        modMenuScreen.settingsOpenTime = 0;
                        modMenuScreen.settingsCloseTime = 0;
                        settingsForMod = mod;
                        modMenuScreen.settingsXOffset = modMenuScreen.settingsWidth;
                        settingsOpen = true;
                    }
                }, null, null, mod + ".settings");
                bagel.setTooltip(Tooltip.of(Text.literal("Settings for " + mod)));
                addDrawableChild(bagel);
                yValue += 2 + 16;
            }

            // make a basic button
//            addDrawableChild(ButtonWidget.builder(Text.literal("Quick Button"), button -> {
//                Mod.setupMods(List.of(Map.of(new Bentifier(), new ModConfig(false))));
//            })
//                .dimensions(20, yValue + 20, 100, 20)
//                .tooltip(Tooltip.of(Text.literal("This is a quick test button.")))
//                .build());

        }

        @Override
        public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
        ) {
            super.render(context, mouseX, mouseY, delta);
            context.drawText(mc.textRenderer, "Gaming Chair | Mod Menu", 20, 20, 0xffffffff, true);

            // Render settings
            // FIXME: fix this stupid hack and make it actually stable
            context.fill(mc.getWindow().getScaledWidth() - settingsWidth + settingsXOffset, 0, mc.getWindow().getScaledWidth() + settingsXOffset, mc.getWindow().getScaledHeight(), 0x80000000);
            context.fill(mc.getWindow().getScaledWidth() - settingsWidth + settingsXOffset, 0, mc.getWindow().getScaledWidth() + settingsXOffset, 20, 0x80000000);
            context.drawText(mc.textRenderer, "Settings for §e" + settingsForMod, mc.getWindow().getScaledWidth() - settingsWidth + 10 + settingsXOffset, 6, 0xffffffff, true);
            context.drawText(mc.textRenderer, "TODO!", mc.getWindow().getScaledWidth() - settingsWidth + 10 + settingsXOffset, 26, 0xffffffff, true);

            if (settingsOpen) {
                settingsOpenTime = Math.min(settingsOpenTime + delta, 6);
                settingsCloseTime = 0;
                settingsXOffset = Math.max((int) ((settingsWidth+2) * Math.exp(-0.8 * settingsOpenTime)), 1) - 1;
            } else {
                settingsCloseTime = Math.min(settingsCloseTime + delta, 6);
                settingsOpenTime = 0;
                settingsXOffset = Math.max((settingsWidth+2) - (int) ((settingsWidth+2) * Math.exp(-0.8 * settingsCloseTime)), 1) - 1;
            }
            Debugger.set("settingsXOffset", String.valueOf(settingsXOffset), 0);
        }

        @Override
        public void close() {
            super.close();
            Mod.exportEnabledMods();
        }
    }

    public static void cycle() {
        modMenuScreen.tick();
        boolean ModMenuToggle = InputUtil.isKeyPressed(
            //? if >=1.21.10 {
            mc.getWindow(),
            //?} else {
            /*mc.getWindow().getHandle(),
            *///?}
            InputUtil.GLFW_KEY_RIGHT_SHIFT
        );
        if (mc.currentScreen == null || mc.currentScreen == modMenuScreen) {
            if (ModMenuToggle && !triggerToggle) {
                toggle();
                triggerToggle = true;
            } else {
                if (!ModMenuToggle && triggerToggle) {
                    triggerToggle = false;
                }
            }
        }
    }

    public static void enable() {
        mc.setScreen(modMenuScreen);
        modMenuScreen.settingsForMod = "";
        modMenuScreen.settingsOpen = false;
        modMenuScreen.settingsOpenTime = 0;
        modMenuScreen.settingsCloseTime = 6;
        modMenuScreen.settingsXOffset = modMenuScreen.settingsWidth;
    }

    public static void disable() {
        mc.setScreen(null);
        Mod.exportEnabledMods();
    }

    public static void toggle() {
        if (mc.currentScreen == modMenuScreen) {
            disable();
        } else {
            enable();
        }
    }
}
