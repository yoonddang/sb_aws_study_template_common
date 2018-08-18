package com.template.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringUtil {



    public static String makeStackTrace(Throwable t){
        if(t == null) return "";
        try{
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(bout));
            bout.flush();
            String error = new String(bout.toByteArray());

            return error;
        }catch(Exception ex){
            return "";
        }
    }

    public static String textValidity(String text) {
        text.replaceAll("&lt;","<")
                .replaceAll("&gt",">")
                .replaceAll("\"","&quot;")
                .replaceAll("'","&#39;")
                .replaceAll("\r\n","<br>");
        return text;
    }

}
