package com.lemon.baseutils.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description: 令牌util
 *
 * @author lemon
 * @date 2019-07-19 16:07:06 创建
 */
public class TokenUtils {

    /**
     * 根据 TokenDetail 生成 Token
     *
     * @param username
     * @return
     */
    public static String generateToken(String username, String password, long expiration, String secret) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", username);
        claims.put("password", password);
        claims.put("created", generateCurrentDate());
        return generateToken(claims, expiration, secret);
    }

    /**
     * 检查 token 是否处于有效
     * @param token
     * @param usernameFlag
     * @param lastPasswordReset
     * @return
     */
    public static Boolean validateToken(String token, String usernameFlag, String passwordFlag, String secret, Date lastPasswordReset) {
        final String username = getUsernameFromToken(token, secret);
        final String password = getPasswordFromToken(token, secret);
        final Date created = getCreatedDateFromToken(token, secret);

        return (username.equals(usernameFlag) &&
                password.equals(passwordFlag) &&
                !(isTokenExpired(token, secret)) &&
                !(isCreatedBeforeLastPasswordReset(created, lastPasswordReset)));
    }

    /**
     * 获得我们封装在 token 中的 token 创建时间
     * @param token
     * @return
     */
    public static Date getCreatedDateFromToken(String token, String secret) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            created = new Date((Long) claims.get("created"));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 获得我们封装在 token 中的 token 过期时间
     * @param token
     * @return
     */
    public static Date getExpirationDateFromToken(String token, String secret) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 根据 claims 生成 Token
     *
     * @param claims
     * @return
     */
    private static String generateToken(Map<String, Object> claims, long expiration, String secret) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate(expiration))
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException ex) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate(expiration))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
    }

    /**
     * 从 token 中拿到 username
     *
     * @param token
     * @return
     */
    public static String getUsernameFromToken(String token, String secret) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            username = (String) claims.get("username");
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从 token 中拿到 password
     *
     * @param token
     * @return
     */
    public static String getPasswordFromToken(String token, String secret) {
        String password;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            password = (String) claims.get("password");
        } catch (Exception e) {
            password = null;
        }
        return password;
    }

    /**
     * 解析 token 的主体 Claims
     *
     * @param token
     * @return
     */
    private static Claims getClaimsFromToken(String token, String secret) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


    /**
     * 检查当前时间是否在封装在 token 中的过期时间之后，若是，则判定为 token 过期
     * @param token
     * @return
     */
    private static Boolean isTokenExpired(String token, String secret) {
        final Date expiration = getExpirationDateFromToken(token, secret);
        return expiration.before(generateCurrentDate());
    }

    /**
     * 检查 token 是否是在最后一次修改密码之前创建的（账号修改密码之后之前生成的 token 即使没过期也判断为无效）
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * token 过期时间
     *
     * @return
     */
    private static Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    private static Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
