package ubersystem.utils;


import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ubersystem.pojo.User;

import java.util.Date;
import java.util.Objects;


public class JwtUtils {
    private static final String SECRET_KEY = "SECRET_KEY";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public static String generateToken(User user) {
        return JWT.create()
                .withClaim("uid", user.getUid())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour token validity
                .sign(algorithm);
    }

    private static DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }

    public static Long getUid(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("uid").asLong();
    }

    public static boolean verifyToken(String token, Long uid) {
        DecodedJWT jwt = verifyToken(token);
        return Objects.equals(jwt.getClaim("uid").asLong(), uid);
    }

    //create token by id
    public static String createTokenById(Long id) {
        return JWT.create()
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour token validity
                .sign(algorithm);
    }

    public static Long getId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("id").asLong();
    }



}
