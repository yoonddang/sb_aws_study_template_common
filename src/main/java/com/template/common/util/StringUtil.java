package com.template.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringUtil {

    public static String textValidity(String text) {
        text.replaceAll("&lt;","<")
                .replaceAll("&gt",">")
                .replaceAll("\"","&quot;")
                .replaceAll("'","&#39;")
                .replaceAll("\r\n","<br>");
        return text;
    }

}
