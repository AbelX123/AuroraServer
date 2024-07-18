package com.aurora.client.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Jwt工具类
 */
public class JwtUtils {

    // 密钥
    private final static String SECRET_KEY = "MSNEUTSSLIVOGPNXIZFGOJBBWJUVSGRL";

    // token过期时间
    private final static Long TOKEN_EXPIRE_TIME = 12 * 60 * 60 * 1000L;

    // refresh_token过期时间
    private final static Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    private final static SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 抽离生成token
     */
    public static String generate(String subject, Long expire) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 生成token
     */
    public static String generateToken(String subject) {
        return generate(subject, TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成refresh_token
     */
    public static String generateRefreshToken(String subject) {
        return generate(subject, REFRESH_TOKEN_EXPIRE_TIME);
    }


    /**
     * 解析token
     */
    public static Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 判定是否过期，这里只能返回未过期的false状态，如果过期会抛出异常 ExpiredJwtException
     */
    public static boolean ifExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    public static Long getExp(String token) {
        Claims claims = parseToken(token);
        // 注意这里拿出来的时间和原本设置的时间是有差距的，原本的时间是精确到毫秒的，这里的时间只精确到秒，但是是以毫秒表示的
        // 1721311782259 --> 1721311782000
        return claims.getExpiration().getTime();
    }
}
