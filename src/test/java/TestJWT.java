
import com.mycompany.jwtauth.JWT;
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
}
