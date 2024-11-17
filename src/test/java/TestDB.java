
import com.mycompany.jwtauth.AbstractDB;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author bhats
 */
public class TestDB extends AbstractDB{

    @Override
    public boolean verifyUser(String username, String password) {
        return "sahana".equals(username) && "12345".equals(password);
    }
    
}
