package magiclibrary.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class EnchantmentsContainer {
    private HashMap<String, Integer> enchantments = new LinkedHashMap<>();

    public List<String> getIdentifiers() {
        var list = new ArrayList<>(enchantments.keySet());
        list.sort((a,b) -> a.compareTo(b));
        return list;
    }

    public boolean put(String identifier, int level) {
        if (identifier == null || identifier.isEmpty()) {
            return false;
        }

        if (enchantments.containsKey(identifier)) {
            var currentLevel = enchantments.get(identifier);
            if (currentLevel < level) {
                enchantments.put(identifier, level);
                return true;
            }
        }
        else {
            enchantments.put(identifier, level);
            return true;
        }

        return false;
    }

    public int get(String identifier) {
        if (enchantments.containsKey(identifier)) {
            return enchantments.get(identifier);
        }
        else {
            return 0;
        }
    }

    public String toString() {
        var elements = new ArrayList<String>();
        for (var entry: enchantments.entrySet()) {
            elements.add(entry.getKey() + "=" + entry.getValue());
        }
        return String.join(",", elements);
    }

    public static EnchantmentsContainer fromString(String string) {
        var enchantments = new EnchantmentsContainer();
        if (string != null && !string.isEmpty()) {
            for (var element: string.split(",")) {
                var parts = element.split("=");
                enchantments.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        return enchantments;
    }
}
