package sh.thered.gamingchair.client.debug;

public class DebugInfo {
    String name;
    String value;
    int priority;

    public DebugInfo(String name, String value, int priority) {
        this.name = name;
        this.value = value;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }
}
