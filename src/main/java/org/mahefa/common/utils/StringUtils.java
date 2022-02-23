package org.mahefa.common.utils;

public final class StringUtils {

    public static String capitalize(String text) {
        if(text == null || text.isEmpty()) {
            return text;
        }

        return (text.length() > 1)
                ? text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase()
                : text.toUpperCase();
    }

    public static boolean isNotBlank(String text) {
        return (text != null && !text.trim().equals(""));
    }
}
