package sh.thered.gamingchair.client.debug;

import java.util.ArrayList;
import java.util.Collection;

public class DebugInfoCollectionBuilder {
    private final String name;
    private final Collection<DebugInfo> debugInfoCollection;

    public DebugInfoCollectionBuilder(String name) {
        this.name = name;
        this.debugInfoCollection = new ArrayList<>();
    }

    public DebugInfoCollectionBuilder add(String name, String value, int priority) {
        this.debugInfoCollection.add(new DebugInfo(name, value, priority));
        return this;
    }

    public String getName() {
        return name;
    }

    public Collection<DebugInfo> build() {
        return debugInfoCollection;
    }
}
