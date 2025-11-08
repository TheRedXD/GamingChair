package sh.thered.gamingchair.client.mods;

import sh.thered.gamingchair.client.Mod;

public class Bentifier extends Mod {
    static String name = "gc.bentifier";
    static String description = "🅱3nti sp34k!";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    public static String chatPass(String message) {
        if (Mod.isDisabled(name)) return message;
        String finalMessage = message
                .replace("then", "d3n")
                .replace("that", "d4t")
                .replace("THAT", "D4T")
                .replace("bc", "kuz")
                .replace("because", "kuz")
                .replace("cause", "kuz")
                .replace("b", "🅱")
                .replace("B", "🅱")
                .replace('o', '0')
                .replace('O', '0')
                .replace('e', '3')
                .replace('E', '3')
                .replace('a', '4')
                .replace('A', '4')
//                .replace("m", "Ⓜ")
//                .replace("M", "Ⓜ")
//                .replace('s', '5')
//                .replace('S', '5')
                .replace('i', '1')
                .replace('I', '1')
                .replace('p', '[')
                .replace('P', '[')
                .replace("🅱3nt1", "🅱3nti");

        if (!finalMessage.endsWith(" xd") && !finalMessage.endsWith("xd")) {
            finalMessage += " xd";
        }
        return finalMessage;
    }
}
