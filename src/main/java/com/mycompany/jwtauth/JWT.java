/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jwtauth;

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
    
    public JWT(AbstractDB dbImpl) {
        this.dbImpl = dbImpl;
    }
    
    public String generateJWT(String username, String password) throws JoseException {
        
        if (!dbImpl.verifyUser(username, password)) {
            return "Invalid userID and/or password.";
        }
        
        String jwt = "";
        
        String jwt_claim = "im da best";
        String jwt_issuer = username;
        String jwt_subject = "life";
        String jwt_id = "sahanabhat@123";
        
        JwtClaims claims = new JwtClaims();
        JsonWebSignature jws = new JsonWebSignature();
        RsaJsonWebKey rsa = RsaJwkGenerator.generateJwk(2084);
        
        claims.setClaim("Claims", jwt_claim);
        claims.setIssuer(jwt_issuer);
        claims.setSubject(jwt_subject);
        claims.setJwtId(jwt_id);
        
        jws.setPayload(claims.toJson());
        jws.setKey(rsa.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        
        //System.out.println("Claims = "+claims);
        //System.out.println("JWS = "+ jws);
        jwt = jws.getCompactSerialization();
        
        return jwt;
    }
}
