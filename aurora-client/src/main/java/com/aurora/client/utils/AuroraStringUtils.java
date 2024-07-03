package com.aurora.client.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public class AuroraStringUtils {

    /**
     * 驼峰法转下划线
     */
    public static String camel2Underline(String str) {

        if (StringUtils.isBlank(str)) {
            return "";
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.append("_").append(Character.toLowerCase(str.charAt(i)));
            } else {
                sb.append(str.charAt(i));
            }
        }
        return (str.charAt(0) + sb.toString()).toLowerCase();
    }
}
