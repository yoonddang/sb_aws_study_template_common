package com.template.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecureUtil {
	private static final Logger logger = LoggerFactory.getLogger(SecureUtil.class);

    public static boolean checkSecureContent(String content) {
        // 널 혹은 공백 검사
        if(StringUtils.isBlank(content)) {
            return false;
        } else {
            content.replaceAll("<","&lt;")
                    .replaceAll(">","&gt")
                    .replaceAll("\"","&quot;")
                    .replaceAll("'","&#39;")
                    .replaceAll("\n","<br>")
                    .replaceAll("\r\n","<br>");
            return true;
        }
    }

}
