/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jwtauth;


import com.nimbusds.jose.JOSEException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

/**
 *
 * @author bhats
 */
public class JWT {

    private AbstractDB dbImpl;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public JWT(AbstractDB dbImpl) throws JoseException {
        this.dbImpl = dbImpl;
        RsaJsonWebKey rsa = RsaJwkGenerator.generateJwk(2084);
        rsa.setKeyId("keyId");
        this.privateKey = rsa.getPrivateKey();
        this.publicKey = rsa.getPublicKey();
    }

    public String generateJWT(String username, String password) throws JoseException {

        if (!dbImpl.verifyUser(username, password)) {
            return "Invalid userID and/or password.";
        }

        String jwt = "";

        JwtClaims claims = new JwtClaims();
        JsonWebSignature jws = new JsonWebSignature();
        //RsaJsonWebKey rsa = RsaJwkGenerator.generateJwk(2084);

        claims.setClaim("Claims", dbImpl.getClaims(username));
        claims.setIssuer(dbImpl.getIssuer(username));
        claims.setSubject(dbImpl.getSubject(username));
        claims.setJwtId(dbImpl.getJWTId(username));
        claims.setIssuedAtToNow();  // Add 'iat' claim
        claims.setExpirationTimeMinutesInTheFuture(30);

        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        //System.out.println("Claims = "+claims);
        //System.out.println("JWS = "+ jws);
        jwt = jws.getCompactSerialization();
        return jwt;
    }

    public boolean decodeJWT(String jwt) throws ParseException, java.text.ParseException, JOSEException {
        String[] split_jwt = jwt.split("\\.");
        /*Base64.Decoder decoder = Base64.getUrlDecoder();
        
        String header = new String(decoder.decode(split_jwt[0]));
        String payload = new String(decoder.decode(split_jwt[1]));
        String lastPart = new String(decoder.decode(split_jwt[2]));
        
        System.out.println("Last part = "+ lastPart);
        
        JSONParser parser = new JSONParser();  
        JSONObject json_header = (JSONObject) parser.parse(header);
        JSONObject json_payload = (JSONObject) parser.parse(payload);
        
        JSONArray decodedJWT = new JSONArray();
        decodedJWT.add(json_header);
        decodedJWT.add(json_payload);
         */

        /*EncryptedJWT encryptedJWT = EncryptedJWT.parse(jwt);
        RSADecrypter decrypter = new RSADecrypter(privateKey);
        encryptedJWT.decrypt(decrypter);
        JWTClaimsSet claims = encryptedJWT.getJWTClaimsSet();
        Payload payload = encryptedJWT.getPayload();
        
        JSONArray decodedJWT = new JSONArray();
        System.out.println("Stuff: ");
        System.out.println(encryptedJWT.toString());
        System.out.println("Stuff: ");
         */
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setVerificationKey(publicKey)
                .build();
        JSONArray decodedJWT = new JSONArray();

        try {
            JwtClaims claims = jwtConsumer.processToClaims(jwt);
            //System.out.println("here0");
            Map<String, List<Object>> claim = claims.flattenClaims();
            //System.out.println("claim = "+claim.toString());
            String iss = claim.get("iss").get(0).toString();
            
            if( iss.equals(dbImpl.getIssuer(iss) )) { // how do i pass username to dbImpl.getIssuer()
                //System.out.println("here1");
                String issuer = (String) claim.get("iss").get(0);
                String sub =  (String) claim.get("sub").get(0);
                String cl =  (String) claim.get("Claims").get(0);
                String jti =  (String) claim.get("jti").get(0);
                //System.out.println("here2");
                if(!sub.equals(dbImpl.getSubject(issuer)) ||
                   !jti.equals(dbImpl.getJWTId(issuer)) ||
                   !cl.equals(dbImpl.getClaims(issuer))) {
                    System.out.println("ERROR INVALID JWT PAYLOAD DATA");
                    return false;
                } else {
                    System.out.println("VALID JWT PAYLOAD DATA");
                    // return false;
                }
            }
            JSONParser parser = new JSONParser();
            JSONObject json_payload = (JSONObject) parser.parse(claims.toJson());
            decodedJWT.add(json_payload);
            
        } catch (Exception e) {
            System.err.println("JWT verification failed: " + e.getMessage());
            return false;
        }
        return true;
    }
}
