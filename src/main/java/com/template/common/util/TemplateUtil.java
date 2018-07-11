package com.template.common.util;

import com.template.common.constants.TemplateConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TemplateUtil {

    public static boolean checkLogin(HttpServletRequest request, HttpServletResponse response, boolean redirect) {
        try {
            if(!(boolean)request.getAttribute(TemplateConstants.IS_LOGIN)) {
                // 비 로그인시 접근 제한

                if(redirect) {
                    response.sendRedirect("/login");
                }
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
