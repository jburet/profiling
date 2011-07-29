package fr.xebia.log.configuration;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class PropertiesUtils {

    public static List<String> convertPropertiesToList(String propertiesBase, Properties properties) {
        List<String> res = new ArrayList<String>();
        TreeMap<Integer, String> propertiesOrdered = new TreeMap<Integer, String>();
        if (properties != null) {
            for (Object key : properties.keySet()) {
                String keyString = (String) key;
                // Start with base.
                if (keyString.startsWith(propertiesBase + ".")) {
                    String indexString = keyString.substring(keyString.indexOf(propertiesBase + ".") + propertiesBase.length() + 1);
                    int index = -1;
                    try {
                        index = Integer.parseInt(indexString);
                    } catch (Exception e) {
                        // No log
                    }
                    if (index >= 0) {
                        propertiesOrdered.put(index, (String) properties.get(keyString));
                    }
                }
            }
        }
        res.addAll(propertiesOrdered.values());
        return res;
    }
}
