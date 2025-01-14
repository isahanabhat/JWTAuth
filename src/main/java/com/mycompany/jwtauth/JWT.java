/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jwtauth;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
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
        
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        
        //System.out.println("Claims = "+claims);
        //System.out.println("JWS = "+ jws);
        jwt = jws.getCompactSerialization();
        return jwt;
    }
    public JSONArray decodeJWT(String jwt) throws ParseException {
        String[] split_jwt = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        
        String header = new String(decoder.decode(split_jwt[0]));
        String payload = new String(decoder.decode(split_jwt[1]));
        
        JSONParser parser = new JSONParser();  
        JSONObject json_header = (JSONObject) parser.parse(header);
        JSONObject json_payload = (JSONObject) parser.parse(payload);
        
        JSONArray decodedJWT = new JSONArray();
        decodedJWT.add(json_header);
        decodedJWT.add(json_payload);
        
        return decodedJWT;
    }
}
