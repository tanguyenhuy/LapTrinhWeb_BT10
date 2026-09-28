package vn.huytan.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            Date now = new Date();
            Date validity = new Date(now.getTime() + jwtExpiration);

            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issueTime(now)
                    .expirationTime(validity);

            if (extraClaims != null) {
                extraClaims.forEach(builder::claim);
            }

            SignedJWT signedJWT = new SignedJWT(header, builder.build());
            JWSSigner signer = new MACSigner(secretKey.getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Nimbus Sign Error: " + e.getMessage(), e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            boolean isSignatureValid = signedJWT.verify(verifier);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean isNotExpired = expiration != null && expiration.after(new Date());
            String username = signedJWT.getJWTClaimsSet().getSubject();

            return isSignatureValid && isNotExpired && username.equals(userDetails.getUsername());
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }
}