package com.selflearning.englishcourses.util;

import java.util.Objects;

public class StringUtils {

    public static String formatToFileName(String text, String extension) {
        if (Objects.isNull(text))
            return null;
        return (text.replaceAll
                ("[(\\,)(\\:)(\\\"+)(\\s)(\\/)(\\?)(\\<)(\\>)(\\{+)(\\})(\\[+)(\\])(\\\\)(\\t)(\\:)(\\-)]+",
                        "-").replaceAll("(\\.+)", "") + "." + extension).toLowerCase();
    }

}
