package com.mage.crm.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request, String str){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals(str)){
                    try {
                        return URLDecoder.decode(cookie.getValue(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
