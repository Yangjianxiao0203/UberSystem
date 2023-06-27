package ubersystem.utils;


import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;


public class JwtUtils {
    private static final String SECRET_KEY = "SECRET_KEY";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public static String generateToken(String subject) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour token validity
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        return verifier.verify(token);
    }

    public static Claim getClaimFromToken(String token, String claimName) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim(claimName);
    }
}
