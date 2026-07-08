package sh.thered.gamingchair.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.mods.Debugger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ModMenu {
    public static ModMenuScreen modMenuScreen = new ModMenuScreen();
    static Minecraft mc = Minecraft.getInstance();
    static boolean triggerToggle = false;

    public static String savedNotepadText = "";

    public static void saveNotepad() {
        try {
            Path path = Paths.get(Minecraft.getInstance().gameDirectory.getAbsolutePath(), "gamingchair_notepad.txt");
            Files.writeString(path, savedNotepadText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadNotepad() {
        try {
            Path path = Paths.get(Minecraft.getInstance().gameDirectory.getAbsolutePath(), "gamingchair_notepad.txt");
            if (Files.exists(path)) {
                savedNotepadText = Files.readString(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasShiftDown() {
        com.mojang.blaze3d.platform.Window window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean hasControlDown() {
        com.mojang.blaze3d.platform.Window window = Minecraft.getInstance().getWindow();
        return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL) ||
                InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_CONTROL) ||
                InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SUPER) ||
                InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SUPER);
    }

    public static boolean isSelectAll(int keyCode) { return hasControlDown() && keyCode == GLFW.GLFW_KEY_A; }
    public static boolean isCopy(int keyCode) { return hasControlDown() && keyCode == GLFW.GLFW_KEY_C; }
    public static boolean isPaste(int keyCode) { return hasControlDown() && keyCode == GLFW.GLFW_KEY_V; }
    public static boolean isCut(int keyCode) { return hasControlDown() && keyCode == GLFW.GLFW_KEY_X; }

    public static class ModMenuButton extends Button {
        public static final int OUTLINE_THICKNESS = 1;
        public static final int BG_COLOR = 0x22FFFFFF; // ARGB: semi-transparent white
        public static final int OUTLINE_COLOR = 0xFFFFFFFF; // ARGB: solid white

        public String buttonModName = "";

        protected ModMenuButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, String modName) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
            buttonModName = modName;
        }

        @Override
        public void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            if (buttonModName.endsWith(".settings")) {
                if (buttonModName.substring(0, buttonModName.lastIndexOf(".")).equals(ModMenuScreen.settingsForMod) && ModMenuScreen.settingsOpen) {
                    int rainbowInt = Utils.getRainbowInt();
                    int transparentRainbowInt = Utils.getRainbowInt() - 0xff000000 | 0x22000000;

                    context.fill(getX(), getY(), getX() + width, getY() + height, transparentRainbowInt);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, rainbowInt); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, rainbowInt); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, rainbowInt); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, rainbowInt); // Right
                } else {
                    context.fill(getX(), getY(), getX() + width, getY() + height, BG_COLOR);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, OUTLINE_COLOR); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, OUTLINE_COLOR); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, OUTLINE_COLOR); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, OUTLINE_COLOR); // Right
                }
            } else if (buttonModName.split("\\.").length > 2 && buttonModName.substring(0, buttonModName.indexOf(".", 1)).equals(ModMenuScreen.settingsForMod + ".settings") && !buttonModName.endsWith(".settings")) {
                if ((Boolean) Objects.requireNonNull(ModConfig.getOption(ModMenuScreen.settingsForMod, buttonModName.split("\\.")[2]))) {
                    int rainbowInt = Utils.getRainbowInt();
                    int transparentRainbowInt = Utils.getRainbowInt() - 0xff000000 | 0x22000000;

                    context.fill(getX(), getY(), getX() + width, getY() + height, transparentRainbowInt);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, rainbowInt); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, rainbowInt); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, rainbowInt); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, rainbowInt); // Right
                } else {
                    context.fill(getX(), getY(), getX() + width, getY() + height, BG_COLOR);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, OUTLINE_COLOR); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, OUTLINE_COLOR); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, OUTLINE_COLOR); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, OUTLINE_COLOR); // Right
                }
            } else {
                if (Mod.isEnabled(buttonModName)) {
                    int rainbowInt = Utils.getRainbowInt();
                    int transparentRainbowInt = Utils.getRainbowInt() - 0xff000000 | 0x22000000;

                    context.fill(getX(), getY(), getX() + width, getY() + height, transparentRainbowInt);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, rainbowInt); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, rainbowInt); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, rainbowInt); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, rainbowInt); // Right
                } else if (Mod.isDisabled(buttonModName)) {
                    context.fill(getX(), getY(), getX() + width, getY() + height, BG_COLOR);
                    context.fill(getX(), getY(), getX() + width, getY() + OUTLINE_THICKNESS, OUTLINE_COLOR); // Top
                    context.fill(getX(), getY() + height - OUTLINE_THICKNESS, getX() + width, getY() + height, OUTLINE_COLOR); // Bottom
                    context.fill(getX(), getY(), getX() + OUTLINE_THICKNESS, getY() + height, OUTLINE_COLOR); // Left
                    context.fill(getX() + width - OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, OUTLINE_COLOR); // Right
                }
            }

            context.text(mc.font, getMessage(), getX() + width / 2 - mc.font.width(getMessage()) / 2, getY() + (height - 8) / 2, 0xFFFFFFFF);
        }
    }

    public static class NotepadButton extends Button {
        public NotepadButton(int x, int y, int width, int height, Component message, Button.OnPress onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        }

        @Override
        public void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + width, getY() + height, ModMenuButton.BG_COLOR);
            context.fill(getX(), getY(), getX() + width, getY() + ModMenuButton.OUTLINE_THICKNESS, ModMenuButton.OUTLINE_COLOR);
            context.fill(getX(), getY() + height - ModMenuButton.OUTLINE_THICKNESS, getX() + width, getY() + height, ModMenuButton.OUTLINE_COLOR);
            context.fill(getX(), getY(), getX() + ModMenuButton.OUTLINE_THICKNESS, getY() + height, ModMenuButton.OUTLINE_COLOR);
            context.fill(getX() + width - ModMenuButton.OUTLINE_THICKNESS, getY(), getX() + width, getY() + height, ModMenuButton.OUTLINE_COLOR);
            context.text(mc.font, getMessage(), getX() + width / 2 - mc.font.width(getMessage()) / 2, getY() + (height - 8) / 2, 0xFFFFFFFF);
        }
    }

    public static class AdvancedNotepadWidget extends AbstractWidget {
        public String text = "";
        private int cursor = 0;
        private int selectAnchor = 0;
        public float fontScale = 1.0f;
        public boolean rawMode = false;
        private int scroll = 0;
        private long lastTypingTime = 0;
        private boolean isDraggingScrollbar = false;

        public AdvancedNotepadWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal("Notepad"));
            this.text = ModMenu.savedNotepadText;
            this.cursor = this.text.length();
            this.selectAnchor = this.cursor;
        }

        public int getSelStart() { return Math.min(cursor, selectAnchor); }
        public int getSelEnd() { return Math.max(cursor, selectAnchor); }
        public String getSelection() { return text.substring(getSelStart(), getSelEnd()); }

        public String getRenderText(String s) {
            return rawMode ? s.replace('§', '&') : s;
        }

        public void applyFormat(String code) {
            lastTypingTime = System.currentTimeMillis();
            if (cursor != selectAnchor) {
                int start = getSelStart();
                int end = getSelEnd();
                String before = text.substring(0, start);
                String selected = text.substring(start, end);
                String after = text.substring(end);

                text = before + "§" + code + selected + "§r" + after;
                cursor = end + 4;
                selectAnchor = cursor;
            } else {
                insertText("§" + code);
            }
        }

        public void insertText(String s) {
            lastTypingTime = System.currentTimeMillis();
            if (cursor != selectAnchor) deleteSelection();
            text = text.substring(0, cursor) + s + text.substring(cursor);
            cursor += s.length();
            selectAnchor = cursor;
        }

        public void deleteSelection() {
            lastTypingTime = System.currentTimeMillis();
            if (cursor == selectAnchor) return;
            int start = getSelStart();
            int end = getSelEnd();
            text = text.substring(0, start) + text.substring(end);
            cursor = start;
            selectAnchor = start;
        }

        class LineInfo {
            String text;
            int startIndex;
            String formatPrefix;
            public LineInfo(String text, int startIndex, String formatPrefix) {
                this.text = text;
                this.startIndex = startIndex;
                this.formatPrefix = formatPrefix;
            }
        }

        private List<LineInfo> getWrappedLines() {
            Font font = Minecraft.getInstance().font;
            List<LineInfo> result = new ArrayList<>();
            int maxLineWidth = (int)((width - 15) / fontScale);
            if (maxLineWidth < 10) maxLineWidth = 10;

            String currentFormat = "";
            int i = 0;
            while (i < text.length()) {
                int nextNewline = text.indexOf('\n', i);
                if (nextNewline == -1) nextNewline = text.length();

                String paragraph = text.substring(i, nextNewline);
                if (paragraph.isEmpty()) {
                    result.add(new LineInfo("", i, currentFormat));
                } else {
                    int pIdx = 0;
                    while (pIdx < paragraph.length()) {
                        int breakIndex = pIdx + 1;
                        int lastSpace = -1;
                        while (breakIndex <= paragraph.length()) {
                            String sub = paragraph.substring(pIdx, breakIndex);
                            if (font.width(currentFormat + getRenderText(sub)) > maxLineWidth && breakIndex > pIdx + 1) {
                                breakIndex--;
                                if (lastSpace != -1 && lastSpace > pIdx) {
                                    breakIndex = lastSpace + 1;
                                }
                                break;
                            }
                            if (breakIndex < paragraph.length() && paragraph.charAt(breakIndex) == ' ') {
                                lastSpace = breakIndex;
                            }
                            breakIndex++;
                        }
                        if (breakIndex > paragraph.length()) breakIndex = paragraph.length();

                        String lineStr = paragraph.substring(pIdx, breakIndex);
                        result.add(new LineInfo(lineStr, i + pIdx, currentFormat));

                        if (!rawMode) {
                            for (int j = 0; j < lineStr.length() - 1; j++) {
                                if (lineStr.charAt(j) == '§') {
                                    char c = lineStr.charAt(j + 1);
                                    if (c == 'r' || c == 'R') currentFormat = "";
                                    else if ("0123456789abcdefABCDEF".indexOf(c) != -1) currentFormat = "§" + c;
                                    else if ("klmnoKLMNO".indexOf(c) != -1) currentFormat += "§" + c;
                                }
                            }
                        } else {
                            currentFormat = "";
                        }

                        pIdx = breakIndex;
                    }
                }
                i = nextNewline + 1;
            }
            if (text.endsWith("\n") || text.isEmpty()) {
                result.add(new LineInfo("", text.length(), currentFormat));
            }
            return result;
        }

        private double[] getMouseCoords(Object event) {
            double mouseX = 0, mouseY = 0;
            boolean foundX = false, foundY = false;
            try {
                if (event.getClass().isRecord()) {
                    for (java.lang.reflect.RecordComponent rc : event.getClass().getRecordComponents()) {
                        String name = rc.getName().toLowerCase();
                        if (!foundX && (name.equals("x") || name.equals("mousex"))) { mouseX = ((Number) rc.getAccessor().invoke(event)).doubleValue(); foundX = true; }
                        else if (!foundY && (name.equals("y") || name.equals("mousey"))) { mouseY = ((Number) rc.getAccessor().invoke(event)).doubleValue(); foundY = true; }
                    }
                }
                Class<?> curr = event.getClass();
                while ((!foundX || !foundY) && curr != null && curr != Object.class) {
                    for (java.lang.reflect.Field f : curr.getDeclaredFields()) {
                        f.setAccessible(true);
                        String name = f.getName().toLowerCase();
                        if (f.getType() == double.class || f.getType() == float.class || f.getType() == int.class) {
                            if (!foundX && (name.equals("x") || name.equals("mousex"))) { mouseX = ((Number) f.get(event)).doubleValue(); foundX = true; }
                            else if (!foundY && (name.equals("y") || name.equals("mousey"))) { mouseY = ((Number) f.get(event)).doubleValue(); foundY = true; }
                        }
                    }
                    curr = curr.getSuperclass();
                }
            } catch (Exception e) {}
            return new double[]{mouseX, mouseY};
        }

        @Override
        public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubled) {
            double[] coords = getMouseCoords(event);
            if (isMouseOver(coords[0], coords[1]) && this.active && this.visible) {
                this.setFocused(true);
                lastTypingTime = System.currentTimeMillis();

                int barX = getX() + width - 15;
                if (coords[0] >= barX) {
                    isDraggingScrollbar = true;
                    updateScrollFromMouse(coords[1]);
                    return true;
                }

                isDraggingScrollbar = false;
                updateCursorFromMouse(coords[0], coords[1]);
                if (!hasShiftDown()) selectAnchor = cursor;
                return true;
            }
            this.setFocused(false);
            isDraggingScrollbar = false;
            return false;
        }

        @Override
        public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
            if (this.isFocused() && this.active && this.visible) {
                lastTypingTime = System.currentTimeMillis();
                double[] coords = getMouseCoords(event);

                if (isDraggingScrollbar) {
                    updateScrollFromMouse(coords[1]);
                    return true;
                }

                updateCursorFromMouse(coords[0], coords[1]);
                return true;
            }
            return false;
        }

        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            if (isMouseOver(mouseX, mouseY) && this.active && this.visible) {
                scroll -= (int)(scrollY * 20);
                clampScroll();
                return true;
            }
            return false;
        }

        private void updateScrollFromMouse(double mouseY) {
            int totalHeight = (int) (getWrappedLines().size() * Minecraft.getInstance().font.lineHeight * fontScale);
            int maxScroll = Math.max(0, totalHeight - height + 10);
            int barY = getY() + 5;
            int barH = height - 10;
            int handleH = Math.max(10, (int) (barH * ((double) height / (totalHeight + 10))));

            double proportion = (mouseY - barY - handleH / 2.0) / (double) (barH - handleH);

            scroll = (int) (proportion * maxScroll);
            clampScroll();
        }

        private void clampScroll() {
            int totalHeight = (int) (getWrappedLines().size() * Minecraft.getInstance().font.lineHeight * fontScale);
            int maxScroll = Math.max(0, totalHeight - height + 10);
            scroll = Math.min(Math.max(0, scroll), maxScroll);
        }

        @Override
        public boolean charTyped(net.minecraft.client.input.CharacterEvent event) {
            if (!this.visible || !this.active || !this.isFocused()) return false;
            lastTypingTime = System.currentTimeMillis();
            try {
                char codePoint = '\0';
                boolean found = false;

                if (event.getClass().isRecord()) {
                    for (java.lang.reflect.RecordComponent rc : event.getClass().getRecordComponents()) {
                        if (rc.getType() == char.class) {
                            codePoint = (Character) rc.getAccessor().invoke(event);
                            found = true; break;
                        } else if (rc.getType() == int.class && !rc.getName().toLowerCase().contains("modifier")) {
                            int cp = (Integer) rc.getAccessor().invoke(event);
                            if (cp >= 32 && cp <= 0x10FFFF) { codePoint = (char) cp; found = true; break; }
                        }
                    }
                }

                if (!found) {
                    Class<?> curr = event.getClass();
                    while (!found && curr != null && curr != Object.class) {
                        for (java.lang.reflect.Field f : curr.getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.getType() == char.class) {
                                codePoint = (Character) f.get(event);
                                found = true; break;
                            } else if (f.getType() == int.class && !f.getName().toLowerCase().contains("modifier")) {
                                int cp = (Integer) f.get(event);
                                if (cp >= 32 && cp <= 0x10FFFF) { codePoint = (char) cp; found = true; break; }
                            }
                        }
                        curr = curr.getSuperclass();
                    }
                }

                if (found) {
                    if ((codePoint >= ' ' && codePoint != 127) || codePoint == '§') {
                        insertText(Character.toString(codePoint));
                        return true;
                    }
                }
            } catch (Exception e) {}
            return false;
        }

        @Override
        public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
            if (!this.visible || !this.active || !this.isFocused()) return false;
            lastTypingTime = System.currentTimeMillis();
            try {
                int keyCode = 0;
                boolean found = false;

                if (event.getClass().isRecord()) {
                    for (java.lang.reflect.RecordComponent rc : event.getClass().getRecordComponents()) {
                        if (rc.getType() == int.class && rc.getName().toLowerCase().contains("key")) {
                            keyCode = (Integer) rc.getAccessor().invoke(event);
                            found = true; break;
                        }
                    }
                }

                if (!found) {
                    Class<?> curr = event.getClass();
                    while (!found && curr != null && curr != Object.class) {
                        for (java.lang.reflect.Field f : curr.getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.getType() == int.class) {
                                int val = (Integer) f.get(event);
                                if (val > 10 && val < 400 && !f.getName().toLowerCase().contains("modifier")) {
                                    keyCode = val; found = true; break;
                                }
                            }
                        }
                        curr = curr.getSuperclass();
                    }
                }

                if (found && keyCode > 0) {
                    if (isSelectAll(keyCode)) { selectAnchor = 0; cursor = text.length(); return true; }
                    if (isCopy(keyCode)) { Minecraft.getInstance().keyboardHandler.setClipboard(getSelection()); return true; }
                    if (isPaste(keyCode)) { insertText(Minecraft.getInstance().keyboardHandler.getClipboard()); return true; }
                    if (isCut(keyCode)) { Minecraft.getInstance().keyboardHandler.setClipboard(getSelection()); deleteSelection(); return true; }

                    if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                        if (cursor != selectAnchor) deleteSelection();
                        else if (cursor > 0) {
                            text = text.substring(0, cursor - 1) + text.substring(cursor);
                            cursor--; selectAnchor = cursor;
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_DELETE) {
                        if (cursor != selectAnchor) deleteSelection();
                        else if (cursor < text.length()) {
                            text = text.substring(0, cursor) + text.substring(cursor + 1);
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
                        insertText("\n"); return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_LEFT) {
                        if (cursor > 0) cursor--;
                        if (!hasShiftDown()) selectAnchor = cursor;
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                        if (cursor < text.length()) cursor++;
                        if (!hasShiftDown()) selectAnchor = cursor;
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_UP) {
                        List<LineInfo> lines = getWrappedLines();
                        for (int i = 0; i < lines.size(); i++) {
                            LineInfo line = lines.get(i);
                            if (cursor >= line.startIndex && cursor <= line.startIndex + line.text.length()) {
                                if (cursor == line.startIndex + line.text.length() && i < lines.size() - 1 && lines.get(i+1).startIndex == cursor) {
                                    continue;
                                }
                                if (i > 0) {
                                    LineInfo prevLine = lines.get(i - 1);
                                    Font font = Minecraft.getInstance().font;
                                    int currentX = font.width(line.formatPrefix + getRenderText(line.text.substring(0, cursor - line.startIndex)));
                                    int bestC = 0;
                                    int bestDist = 9999;
                                    for (int ch = 0; ch <= prevLine.text.length(); ch++) {
                                        int dist = Math.abs(font.width(prevLine.formatPrefix + getRenderText(prevLine.text.substring(0, ch))) - currentX);
                                        if (dist < bestDist) { bestDist = dist; bestC = ch; }
                                    }
                                    cursor = prevLine.startIndex + bestC;
                                    if (!hasShiftDown()) selectAnchor = cursor;
                                }
                                break;
                            }
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_DOWN) {
                        List<LineInfo> lines = getWrappedLines();
                        for (int i = 0; i < lines.size(); i++) {
                            LineInfo line = lines.get(i);
                            if (cursor >= line.startIndex && cursor <= line.startIndex + line.text.length()) {
                                if (cursor == line.startIndex + line.text.length() && i < lines.size() - 1 && lines.get(i+1).startIndex == cursor) {
                                    continue;
                                }
                                if (i < lines.size() - 1) {
                                    LineInfo nextLine = lines.get(i + 1);
                                    Font font = Minecraft.getInstance().font;
                                    int currentX = font.width(line.formatPrefix + getRenderText(line.text.substring(0, cursor - line.startIndex)));
                                    int bestC = 0;
                                    int bestDist = 9999;
                                    for (int ch = 0; ch <= nextLine.text.length(); ch++) {
                                        int dist = Math.abs(font.width(nextLine.formatPrefix + getRenderText(nextLine.text.substring(0, ch))) - currentX);
                                        if (dist < bestDist) { bestDist = dist; bestC = ch; }
                                    }
                                    cursor = nextLine.startIndex + bestC;
                                    if (!hasShiftDown()) selectAnchor = cursor;
                                }
                                break;
                            }
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_HOME) {
                        List<LineInfo> lines = getWrappedLines();
                        for (int i = 0; i < lines.size(); i++) {
                            LineInfo line = lines.get(i);
                            if (cursor >= line.startIndex && cursor <= line.startIndex + line.text.length()) {
                                if (cursor == line.startIndex + line.text.length() && i < lines.size() - 1 && lines.get(i+1).startIndex == cursor) {
                                    continue;
                                }
                                cursor = line.startIndex;
                                if (!hasShiftDown()) selectAnchor = cursor;
                                break;
                            }
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_END) {
                        List<LineInfo> lines = getWrappedLines();
                        for (int i = 0; i < lines.size(); i++) {
                            LineInfo line = lines.get(i);
                            if (cursor >= line.startIndex && cursor <= line.startIndex + line.text.length()) {
                                if (cursor == line.startIndex + line.text.length() && i < lines.size() - 1 && lines.get(i+1).startIndex == cursor) {
                                    continue;
                                }
                                cursor = line.startIndex + line.text.length();
                                if (!hasShiftDown()) selectAnchor = cursor;
                                break;
                            }
                        }
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
                        scroll -= height - 20;
                        clampScroll();
                        return true;
                    }
                    if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
                        scroll += height - 20;
                        clampScroll();
                        return true;
                    }
                }
            } catch (Exception e) {}
            return false;
        }

        @Override
        public void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + width, getY() + height, 0x88000000);

            Font font = Minecraft.getInstance().font;
            List<LineInfo> lines = getWrappedLines();

            int cursorLine = 0;
            for (int i = 0; i < lines.size(); i++) {
                LineInfo line = lines.get(i);
                if (cursor >= line.startIndex && cursor <= line.startIndex + line.text.length()) {
                    cursorLine = i;
                    if (cursor < line.startIndex + line.text.length() || i == lines.size() - 1 || lines.get(i+1).startIndex > cursor) {
                        break;
                    }
                }
            }

            if (!isDraggingScrollbar) {
                int cursorY = (int) (cursorLine * font.lineHeight * fontScale);
                if (cursorY < scroll) scroll = cursorY;
                if (cursorY + (font.lineHeight * fontScale) > scroll + height - 10)
                    scroll = (int) (cursorY + (font.lineHeight * fontScale) - height + 10);
            }
            clampScroll();

            try {
                context.enableScissor(getX(), getY(), getX() + width, getY() + height);
            } catch (Exception e) {}

            context.pose().pushMatrix();
            context.pose().translate((float) (getX() + 5), (float) (getY() + 5 - scroll));
            context.pose().scale(fontScale, fontScale);

            int yOffset = 0;

            for (int i = 0; i < lines.size(); i++) {
                LineInfo line = lines.get(i);

                if ((yOffset + font.lineHeight) * fontScale >= scroll - font.lineHeight * 2 && yOffset * fontScale <= scroll + height + font.lineHeight * 2) {

                    context.text(font, line.formatPrefix + getRenderText(line.text), 0, yOffset, 0xFFFFFFFF, false);

                    for (int ch = 0; ch <= line.text.length(); ch++) {
                        int globalIdx = line.startIndex + ch;
                        boolean isCursor = (globalIdx == cursor);
                        boolean isSelected = (globalIdx >= getSelStart() && globalIdx < getSelEnd());

                        int strWidth = font.width(line.formatPrefix + getRenderText(line.text.substring(0, ch)));

                        if (isSelected) {
                            int charWidth = (ch < line.text.length()) ? font.width(line.formatPrefix + getRenderText(line.text.substring(0, ch + 1))) - strWidth : 4;
                            context.fill(strWidth, yOffset, strWidth + charWidth, yOffset + font.lineHeight, 0x880055FF);
                        }

                        if (isCursor && isFocused() && ((System.currentTimeMillis() - lastTypingTime < 500) || (System.currentTimeMillis() / 500 % 2 == 0))) {
                            context.fill(strWidth, yOffset, strWidth + 1, yOffset + font.lineHeight, 0xFFFFFFFF);
                        }
                    }
                }

                yOffset += font.lineHeight;
            }
            context.pose().popMatrix();
            try {
                context.disableScissor();
            } catch (Exception e) {}

            int totalHeight = (int) (lines.size() * font.lineHeight * fontScale);
            int maxScroll = Math.max(0, totalHeight - height + 10);
            if (maxScroll > 0) {
                int barX = getX() + width - 10;
                int barY = getY() + 5;
                int barH = height - 10;
                int handleH = Math.max(10, (int) (barH * ((double) height / (totalHeight + 10))));
                int handleY = barY + (int) ((barH - handleH) * ((double) scroll / maxScroll));
                context.fill(barX, barY, barX + 5, barY + barH, 0x55FFFFFF);
                context.fill(barX, handleY, barX + 5, handleY + handleH, 0xFFFFFFFF);
            }
        }

        private void updateCursorFromMouse(double mouseX, double mouseY) {
            double localX = (mouseX - getX() - 5) / fontScale;
            double localY = (mouseY - getY() - 5 + scroll) / fontScale;

            Font font = Minecraft.getInstance().font;
            List<LineInfo> lines = getWrappedLines();
            int yOffset = 0;

            for (int i = 0; i < lines.size(); i++) {
                if (localY >= yOffset && localY < yOffset + font.lineHeight) {
                    LineInfo line = lines.get(i);
                    int bestC = 0;
                    int bestDist = 9999;
                    for (int ch = 0; ch <= line.text.length(); ch++) {
                        int w = font.width(line.formatPrefix + getRenderText(line.text.substring(0, ch)));
                        int dist = (int)Math.abs(w - localX);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestC = ch;
                        }
                    }
                    cursor = line.startIndex + bestC;
                    return;
                }
                yOffset += font.lineHeight;
            }
            if (!lines.isEmpty() && localY >= yOffset) {
                cursor = text.length();
            } else if (!lines.isEmpty() && localY < 0) {
                cursor = 0;
            }
        }

        @Override
        public void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {}
    }


    @Environment(EnvType.CLIENT)
    public static class ModMenuScreen extends Screen {

        protected ModMenuScreen() {
            super(Component.literal("ModMenu"));
        }

        public Button button1;
        public Button button2;

        public static String settingsForMod = "";
        public static boolean settingsOpen = false;
        public static int settingsWidth = 200;
        public static int settingsXOffset = settingsWidth;
        public static double settingsOpenTime = 0;
        public static double settingsCloseTime = 0;

        public static Map<String, AbstractWidget> createdOptions = new HashMap<>();

        public AdvancedNotepadWidget notepadWidget;
        public List<AbstractWidget> notepadToolbar = new ArrayList<>();

        @Override
        public void onClose() {
            ModMenu.disable();
            super.onClose();
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            if (this.notepadWidget != null && this.notepadWidget.active && this.notepadWidget.visible) {
                if (this.notepadWidget.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
                    return true;
                }
            }
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        @Override
        public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
            if (this.notepadWidget != null && this.notepadWidget.active && this.notepadWidget.visible) {
                if (this.notepadWidget.mouseDragged(event, dragX, dragY)) {
                    return true;
                }
            }
            return super.mouseDragged(event, dragX, dragY);
        }

        @Override
        public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubled) {
            if (this.notepadWidget != null && this.notepadWidget.active && this.notepadWidget.visible) {
                if (this.notepadWidget.mouseClicked(event, doubled)) {
                    this.setFocused(this.notepadWidget);
                    return true;
                }
            }

            boolean handled = super.mouseClicked(event, doubled);

            if (handled && this.notepadToolbar.contains(this.getFocused())) {
                AbstractWidget btn = (AbstractWidget) this.getFocused();
                if (btn != null) btn.setFocused(false);
                this.setFocused(this.notepadWidget);
                if (this.notepadWidget != null) this.notepadWidget.setFocused(true);
            }

            return handled;
        }

        @Override
        public boolean charTyped(net.minecraft.client.input.CharacterEvent event) {
            if (this.notepadWidget != null && this.notepadWidget.active && this.notepadWidget.visible) {
                if (this.notepadWidget.charTyped(event)) {
                    return true;
                }
            }
            return super.charTyped(event);
        }

        @Override
        public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
            if (this.notepadWidget != null && this.notepadWidget.active && this.notepadWidget.visible) {
                if (this.notepadWidget.keyPressed(event)) {
                    return true;
                }
            }
            return super.keyPressed(event);
        }

        @Override
        protected void init() {
            int startingYValue = 40;
            int yValue = startingYValue;
            String[] sortedStates = Mod.getStates().stream().sorted(Comparator.naturalOrder()).toArray(String[]::new);
            for (String mod : sortedStates) {
                ModMenuButton bagel = new ModMenuButton(20, yValue, 100, 16, Component.literal(mod), button -> {
                    Mod.setState(mod, !Mod.isEnabled(mod));
                    button.setTooltip(Tooltip.create(Component.literal(Mod.getMod(mod).getDescription() + "\n\n§oCurrently " + (Mod.isEnabled(mod) ? "§aenabled" : "§cdisabled") + ".")));
                }, mod);
                bagel.setTooltip(Tooltip.create(Component.literal(Mod.getMod(mod).getDescription() + "\n\n§oCurrently " + (Mod.isEnabled(mod) ? "§aenabled" : "§cdisabled") + ".")));
                addRenderableWidget(bagel);
                yValue += 2 + 16;
            }
            yValue = startingYValue;
            for (String mod : sortedStates) {
                ModMenuButton bagel = new ModMenuButton(20 + 100 + 2, yValue, 16, 16, Component.literal(">"), button -> {
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
                        for (AbstractWidget value : createdOptions.values()) {
                            removeWidget(value);
                        }
                        createdOptions.clear();
                    }
                }, mod + ".settings");
                bagel.setTooltip(Tooltip.create(Component.literal("Settings for " + mod)));
                if (ModConfig.hasOptions(mod)) {
                    addRenderableWidget(bagel);
                }
                yValue += 2 + 16;
            }

            // notepad setup
            int notepadX = mc.getWindow().getGuiScaledWidth() - 250;
            int notepadY = 75;
            int notepadW = 240;
            int notepadH = mc.getWindow().getGuiScaledHeight() - 85;

            notepadWidget = new AdvancedNotepadWidget(notepadX, notepadY, notepadW, notepadH);
            addRenderableWidget(notepadWidget);

            int btnX = notepadX;
            int btnY = notepadY - 20;

            String[] colors = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
            int cx = btnX;
            for (String c : colors) {
                NotepadButton cb = new NotepadButton(cx, btnY - 20, 15, 16, Component.literal("§" + c + "■"), b -> notepadWidget.applyFormat(c));
                notepadToolbar.add(cb);
                cx += 15;
            }

            notepadToolbar.add(new NotepadButton(btnX, btnY, 20, 16, Component.literal("A-"), b -> notepadWidget.fontScale = Math.max(0.5f, notepadWidget.fontScale - 0.1f)));
            notepadToolbar.add(new NotepadButton(btnX + 22, btnY, 20, 16, Component.literal("A+"), b -> notepadWidget.fontScale = Math.min(3.0f, notepadWidget.fontScale + 0.1f)));
            notepadToolbar.add(new NotepadButton(btnX + 44, btnY, 20, 16, Component.literal("§lB"), b -> notepadWidget.applyFormat("l")));
            notepadToolbar.add(new NotepadButton(btnX + 66, btnY, 20, 16, Component.literal("§oI"), b -> notepadWidget.applyFormat("o")));
            notepadToolbar.add(new NotepadButton(btnX + 88, btnY, 20, 16, Component.literal("§nU"), b -> notepadWidget.applyFormat("n")));
            notepadToolbar.add(new NotepadButton(btnX + 110, btnY, 20, 16, Component.literal("§mS"), b -> notepadWidget.applyFormat("m")));
            notepadToolbar.add(new NotepadButton(btnX + 132, btnY, 40, 16, Component.literal("Reset"), b -> notepadWidget.applyFormat("r")));
            notepadToolbar.add(new NotepadButton(btnX + 174, btnY, 66, 16, Component.literal("Raw: §cOFF"), b -> {
                notepadWidget.rawMode = !notepadWidget.rawMode;
                b.setMessage(Component.literal("Raw: " + (notepadWidget.rawMode ? "§aON" : "§cOFF")));
            }));

            for (AbstractWidget w : notepadToolbar) {
                addRenderableWidget(w);
            }
        }

        @Override
        public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
            super.extractBackground(context, mouseX, mouseY, delta);
            context.text(mc.font, "Gaming Chair | Mod Menu", 20, 20, 0xffffffff, true);

            boolean showNotepad = !settingsOpen && settingsXOffset >= settingsWidth - 1;

            if (notepadWidget != null) {
                notepadWidget.visible = showNotepad;
                notepadWidget.active = showNotepad;
                for (AbstractWidget w : notepadToolbar) {
                    w.visible = showNotepad;
                    w.active = showNotepad;
                }
                if (showNotepad) {
                    context.text(mc.font, "Personal Notepad", notepadWidget.getX(), notepadWidget.getY() - 55, 0xffffffff, true);
                }
            }

            context.fill(mc.getWindow().getGuiScaledWidth() - settingsWidth + settingsXOffset, 0, mc.getWindow().getGuiScaledWidth() + settingsXOffset, mc.getWindow().getGuiScaledHeight(), 0x80000000);
            context.fill(mc.getWindow().getGuiScaledWidth() - settingsWidth + settingsXOffset, 0, mc.getWindow().getGuiScaledWidth() + settingsXOffset, 20, 0x80000000);
            context.text(mc.font, "Settings for §e" + settingsForMod, mc.getWindow().getGuiScaledWidth() - settingsWidth + 10 + settingsXOffset, 6, 0xffffffff, true);

            Debugger.set("settingsXOffset", String.valueOf(settingsXOffset), 0);
            if (ModConfig.hasOptions(settingsForMod)) {
                int yValue = 0;
                for (Map.Entry<String, Object> entry : Objects.requireNonNull(ModConfig.getOptions(settingsForMod)).entrySet()) {
                    String optionName = entry.getKey();
                    Object optionValue = entry.getValue();

                    if (optionValue instanceof Boolean) {
                        boolean isEnabled = (Boolean) optionValue;

                        if (!createdOptions.containsKey(settingsForMod + ".settings." + optionName)) {
                            ModMenuButton option = new ModMenuButton(mc.getWindow().getGuiScaledWidth() - settingsWidth + 10 + settingsXOffset, 26 + yValue, 180, 16, Component.literal(optionName + " " + (isEnabled ? "§a+" : "§c-")), button -> {
                                ModConfig.setOption(settingsForMod, optionName, !isEnabled);
                                for (AbstractWidget value : createdOptions.values()) {
                                    removeWidget(value);
                                }
                                createdOptions.clear();
                            }, settingsForMod + ".settings." + optionName);
                            option.setTooltip(Tooltip.create(Component.literal("" + isEnabled)));
                            addRenderableWidget(option);
                            createdOptions.put(settingsForMod + ".settings." + optionName, option);
                        } else {
                            ModMenuButton option = (ModMenuButton) createdOptions.get(settingsForMod + ".settings." + optionName);
                            option.setX(mc.getWindow().getGuiScaledWidth() - settingsWidth + 10 + settingsXOffset);
                        }
                    }

                    yValue += 20;
                }
            }

            if (settingsOpen) {
                settingsOpenTime = Math.min(settingsOpenTime + delta, 6);
                settingsCloseTime = 0;
                settingsXOffset = Math.max((int) ((settingsWidth+2) * Math.exp(-0.8 * settingsOpenTime)), 1) - 1;
            } else {
                settingsCloseTime = Math.min(settingsCloseTime + delta, 6);
                settingsOpenTime = 0;
                settingsXOffset = Math.max((settingsWidth+2) - (int) ((settingsWidth+2) * Math.exp(-0.8 * settingsCloseTime)), 1) - 1;
                for (AbstractWidget value : createdOptions.values()) {
                    removeWidget(value);
                }
                createdOptions.clear();
            }
        }

        @Override
        public void clearWidgets() {
            super.clearWidgets();
            Mod.exportEnabledMods();
            notepadToolbar.clear();
        }
    }

    public static void cycle() {
        if (modMenuScreen != null) {
            modMenuScreen.tick();
        }
        boolean ModMenuToggle = InputConstants.isKeyDown(
                mc.getWindow(),
                GLFW.GLFW_KEY_RIGHT_SHIFT
        );

        if (mc.screen == modMenuScreen && modMenuScreen.notepadWidget != null && modMenuScreen.notepadWidget.isFocused()) {
            ModMenuToggle = false;
        }

        if (mc.screen == null || mc.screen == modMenuScreen) {
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
        loadNotepad();
        mc.setScreen(modMenuScreen);
        modMenuScreen.settingsForMod = "";
        modMenuScreen.settingsOpen = false;
        modMenuScreen.settingsOpenTime = 0;
        modMenuScreen.settingsCloseTime = 6;
        modMenuScreen.settingsXOffset = modMenuScreen.settingsWidth;
    }

    public static void disable() {
        if (modMenuScreen.notepadWidget != null) {
            savedNotepadText = modMenuScreen.notepadWidget.text;
            saveNotepad();
        }
        mc.setScreen(null);
        Mod.exportEnabledMods();
    }

    public static void toggle() {
        if (mc.screen == modMenuScreen) {
            disable();
        } else {
            enable();
        }
    }
}