package com.poc.merge_poc.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class JwtTokenUtil {
    public static String generateToken(Long userId, String secretKey) {
        // Calculate expiration time
        Date exp = new Date(System.currentTimeMillis() + 360 * 1000);
        Key hmacKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        // Create the payload
        String token = Jwts.builder()
                .claim("user_id", userId)
                .claim("token_type", "access")
                .claim("jti", UUID.randomUUID().toString().replace("-", ""))
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();
        return "Bearer " + token;

    }

}
