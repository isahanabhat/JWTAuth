
import com.mycompany.jwtauth.JWT;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.jose4j.lang.JoseException;
import org.junit.Assert;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author bhats
 */
public class TestJWT {
    @Test
    public void test_01() throws JoseException {
        String user1 = "sahana";
        String pwd1 = "12345";
        
        TestDB dbTest = new TestDB();
        JWT j = new JWT(dbTest);
        
        String jwt1 = j.generateJWT(user1, pwd1);
        //System.out.println(jwt1);
        Assert.assertNotEquals("Invalid userID and/or password.", jwt1);
    }
    
    @Test
    public void test_02() throws JoseException {
        String user1 = "sahanabhat";
        String pwd1 = "12345";
        
        TestDB dbTest = new TestDB();
        JWT j = new JWT(dbTest);
        
        String jwt1 = j.generateJWT(user1, pwd1);
        Assert.assertEquals("Invalid userID and/or password.", jwt1);
    }
    
    @Test
    public void test_03() throws JoseException {
        String user1 = "sahana";
        String pwd1 = "1234567890";
        
        TestDB dbTest = new TestDB();
        JWT j = new JWT(dbTest);
        
        String jwt1 = j.generateJWT(user1, pwd1);
        Assert.assertEquals("Invalid userID and/or password.", jwt1);
    }
    @Test
    public void test_04() throws JoseException, ParseException {
        String user1 = "sahana";
        String pwd1 = "12345";
        
        TestDB dbTest = new TestDB();
        JWT j = new JWT(dbTest);
        
        String jwt1 = j.generateJWT(user1, pwd1);
        JSONArray testing = j.decodeJWT(jwt1);
        
        JSONObject header = (JSONObject) testing.get(0);
        JSONObject payload = (JSONObject) testing.get(1);
        
        Assert.assertEquals(payload.get("Claims"), dbTest.getClaims(user1));
        Assert.assertEquals(payload.get("iss"), dbTest.getIssuer(user1));
        Assert.assertEquals(payload.get("sub"), dbTest.getSubject(user1));
        Assert.assertEquals(payload.get("jti"), dbTest.getJWTId(user1));
    }
}
