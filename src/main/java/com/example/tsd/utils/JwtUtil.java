package com.example.tsd.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.example.tsd.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.WebUtils;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class JwtUtil {
    // 过期时间
    private static final int EXPIRE_TIME = 10;

    // 签发人
    private static final String ISSUER = "rrr";

    // 密钥
    private static final String SECRET_KEY = "my_jwt_key";

    /**
     * 创建token
     *
     * @param json 需要放入token的参数
     * @return
     */
    public static String generateToken(JSONObject json) {
        // 加密方式
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String jwtToken = JWT.create()
                .withSubject(json.toString())
                .withIssuer(ISSUER)
                .withExpiresAt(DateUtil.offsetMinute(new Date(), EXPIRE_TIME))
                .withClaim("userId", json)
                .sign(algorithm);
        return jwtToken;
    }

    /**
     * 验证token
     *
     * @param token
     */
    public static Boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            log.info("Verified token:{}", token);

            DecodedJWT decodedJWT = verifier.verify(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            log.info("过期时间：{}", expiresAt);
        } catch (IllegalArgumentException e) {
            throw new LoginException(500, "登录失败");
        }
        return true;
    }

    /**
     * 获取token过期日期
     *
     * @return
     */
    public static Date getExpireDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, EXPIRE_TIME);
        // 或者
        // DateTime dateTime = DateUtil.offsetMinute(new Date(), 10);
        return calendar.getTime();
    }

    /**
     * 解析token并获取信息
     *
     * @param token JWT令牌
     * @return 包含令牌信息的 JSONObject 对象
     */
    public static JSONObject parseToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String subject = decodedJWT.getSubject();
            JSONObject json = new JSONObject(subject);

            return json;
        } catch (JWTVerificationException e) {
            // 令牌无效或过期
            throw new RuntimeException("Invalid or expired token.");
        }
    }

}
