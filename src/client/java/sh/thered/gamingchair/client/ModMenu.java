package sh.thered.gamingchair.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Comparator;
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

            int yValue = 40;
            String[] sortedStates = Mod.getStates().stream().sorted(Comparator.naturalOrder()).toArray(String[]::new);
            for (String mod : sortedStates) {
                ModMenuButton bagel = new ModMenuButton(20, yValue, 100, 16, Text.literal(mod), button -> {
                    Mod.setState(mod, !Mod.isEnabled(mod));
                }, null, null, mod);
                addDrawableChild(bagel);
                yValue += 2+16;
            }
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
    }

    public static void disable() {
        mc.setScreen(null);
    }

    public static void toggle() {
        if (mc.currentScreen == modMenuScreen) {
            disable();
        } else {
            enable();
        }
    }
}
