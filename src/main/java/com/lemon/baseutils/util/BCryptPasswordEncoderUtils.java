package com.lemon.baseutils.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * description: 密码加密与验证
 *
 * @author lemon
 * @date 2019-07-25 10:20:06 创建
 */
public class BCryptPasswordEncoderUtils {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String encode(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    public static boolean match(String rawPassword,  String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
