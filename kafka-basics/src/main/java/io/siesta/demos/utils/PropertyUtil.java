package io.siesta.demos.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
