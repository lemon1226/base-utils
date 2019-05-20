package com.lemon.baseutils.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 驼峰命名转换
 *
 * @author lemon
 * @date 2019-04-25 11:12:06 创建
 */
public class CaseUtils {

    private static Pattern p = Pattern.compile("_(.)");

    public static String camelCase(String origin) {
        Matcher matcher = p.matcher(origin);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String snakeCase(String origin) {
        return origin.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

}
