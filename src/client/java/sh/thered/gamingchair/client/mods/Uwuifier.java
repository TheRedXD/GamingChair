package sh.thered.gamingchair.client.mods;

import sh.thered.gamingchair.client.Mod;
import sh.thered.gamingchair.client.ModConfig;

import java.util.*;
import java.util.regex.*;

public class Uwuifier extends Mod {
    static String name = "gc.uwuifier";
    static String description = "...I think it's quite obvious what this does.";

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public void init() {
        ModConfig.setOption(getName(), "force_sfw_actions", false);
        ModConfig.setOption(getName(), "actions", true);
        ModConfig.setOption(getName(), "faces", true);
        ModConfig.setOption(getName(), "exclamations", true);
    }

    private static final String[] FACES = {
            "(・`ω´・)", ";;w;;", "OwO", "UwU", ">w<", "^w^", "ÚwÚ", "^-^", ":3", "x3"
    };
    private static final String[] EXCLAMATIONS = {
            "!?","?!!","?!?1","!!11","?!?!"
    };
    private static final String[] ACTIONS = {
            "*blushes*","*whispers to self*","*cries*","*screams*","*sweats*",
            "*twerks*","*runs away*","*screeches*","*walks away*","*sees bulge*",
            "*looks at you*","*notices buldge*","*starts twerking*","*huggles tightly*","*boops your nose*"
    };
    private static final String[] SFW_ACTIONS = {
            "*blushes*","*whispers to self*","*cries*","*screams*","*sweats*",
            "*runs away*","*screeches*","*walks away*",
            "*looks at you*","*huggles tightly*","*boops your nose*"
    };
    private static final List<PatternReplacement> UWU_MAP = Arrays.asList(
            new PatternReplacement(Pattern.compile("(?:r|l)", Pattern.UNICODE_CASE), "w"),
            new PatternReplacement(Pattern.compile("(?:R|L)", Pattern.UNICODE_CASE), "W"),
            new PatternReplacement(Pattern.compile("n([aeiou])", Pattern.UNICODE_CASE), "ny$1"),
            new PatternReplacement(Pattern.compile("n([AEIOU])", Pattern.UNICODE_CASE), "nY$1"),
            new PatternReplacement(Pattern.compile("N([aeiou])", Pattern.UNICODE_CASE), "Ny$1"),
            new PatternReplacement(Pattern.compile("N([AEIOU])", Pattern.UNICODE_CASE), "NY$1"),
            new PatternReplacement(Pattern.compile("ove", Pattern.UNICODE_CASE), "uv"),
            new PatternReplacement(Pattern.compile("OVE", Pattern.UNICODE_CASE), "UV"),
            new PatternReplacement(Pattern.compile("oVE", Pattern.UNICODE_CASE), "uV"),
            new PatternReplacement(Pattern.compile("Ove", Pattern.UNICODE_CASE), "Uv"),
            new PatternReplacement(Pattern.compile("OVe", Pattern.UNICODE_CASE), "UV"),
            new PatternReplacement(Pattern.compile("oVe", Pattern.UNICODE_CASE), "uv"),
            new PatternReplacement(Pattern.compile("OvE", Pattern.UNICODE_CASE), "UV")
    );

    private final double spacesFacesModifier;
    private final double spacesActionsModifier;
    private final double spacesStuttersModifier;
    private final double wordsModifier;
    private final double exclamationsModifier;
    private final Random random;

    public Uwuifier() {
        this(0.05, 0.075, 0.10, 1.0, 1.0);
    }

    public Uwuifier(double faces, double actions, double stutters,
                    double wordsModifier, double exclamationsModifier) {
        this.spacesFacesModifier = faces;
        this.spacesActionsModifier = actions;
        this.spacesStuttersModifier = stutters;
        this.wordsModifier = wordsModifier;
        this.exclamationsModifier = exclamationsModifier;
        this.random = new Random();
    }

    public String uwuifySentence(String input) {
        if (input == null) return null;
        String s = input;
        s = uwuifyWords(s);
        if ((Boolean) ModConfig.getOptionAssertive(name, "exclamations")) s = uwuifyExclamations(s);
        s = uwuifySpaces(s);
        return s;
    }

    private String uwuifyWords(String input) {
        String[] parts = input.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String word = parts[i];
            if (word.matches("https?://\\S+")) {
                continue;
            }
            if (random.nextDouble() <= wordsModifier) {
                for (PatternReplacement pr : UWU_MAP) {
                    word = pr.pattern.matcher(word).replaceAll(pr.replacement);
                }
            }
            parts[i] = word;
        }
        return String.join(" ", parts);
    }

    private String uwuifyExclamations(String input) {
        String[] parts = input.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String word = parts[i];
            Matcher m = Pattern.compile("[?!]+$").matcher(word);
            if (m.find() && random.nextDouble() <= exclamationsModifier) {
                String replacement = EXCLAMATIONS[random.nextInt(EXCLAMATIONS.length)];
                word = word.substring(0, m.start()) + replacement;
            }
            parts[i] = word;
        }
        return String.join(" ", parts);
    }

    private String uwuifySpaces(String input) {
        String[] parts = input.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String word = parts[i];
            double r = random.nextDouble();
            if (r < spacesFacesModifier) {
                if ((Boolean) ModConfig.getOptionAssertive(name, "faces")) word += " " + FACES[random.nextInt(FACES.length)];
            } else if (r < spacesFacesModifier + spacesActionsModifier) {
                if ((Boolean) ModConfig.getOptionAssertive(name, "actions")) {
                    if ((Boolean) ModConfig.getOptionAssertive(name, "force_sfw_actions")) {
                        word += " " + SFW_ACTIONS[random.nextInt(SFW_ACTIONS.length)];
                    } else {
                        word += " " + ACTIONS[random.nextInt(ACTIONS.length)];
                    }
                }
            } else if (r < spacesFacesModifier + spacesActionsModifier + spacesStuttersModifier) {
                if (word.length() > 0 && Character.isLetter(word.charAt(0))) {
                    char first = word.charAt(0);
                    word = first + "-" + word;
                }
            }
            parts[i] = word;
        }
        return String.join(" ", parts);
    }

    private static class PatternReplacement {
        final Pattern pattern;
        final String replacement;
        PatternReplacement(Pattern p, String r) {
            this.pattern = p;
            this.replacement = r;
        }
    }

    public static String chatPass(String inputString) {
        if (isDisabled(name)) return inputString;
        return new Uwuifier().uwuifySentence(inputString);
    }
}
