
import com.mycompany.jwtauth.AbstractDB;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author bhats
 */
public class TestDB extends AbstractDB{
    
    private HashMap<String, List<String>> database = new HashMap<>();
    
    public TestDB() {
        this.database.put("sahana", new ArrayList<>(Arrays.asList("im da best", "sahana", "12345", "life", "sahanabhat@123")));
        this.database.put("varsha", new ArrayList<>(Arrays.asList("im a design kid", "varsha", "67890", "death", "varsha_c")));
        this.database.put("anika", new ArrayList<>(Arrays.asList("im an architect", "anika", "13579", "uklmao", "anika_kohli")));
        this.database.put("taruni", new ArrayList<>(Arrays.asList("im an electric kid", "taruni", "24680", "jugaad", "tarunik_101")));
        this.database.put("maria", new ArrayList<>(Arrays.asList("design head bro", "maria", "39725", "theatre", "mariobros")));
    }
    
    @Override
    public boolean verifyUser(String username, String password) {
        ArrayList<String> info = (ArrayList<String>) database.get(username);
        if(info == null) {
            System.out.println("Not in database");
            return false; 
        }
        return username.equals(info.get(1)) && password.equals(info.get(2));
    }
    
    @Override
    public String getClaims(String username) {
        ArrayList<String> info = (ArrayList<String>) database.get(username);
        return info.get(0);
    }
    @Override
    public String getIssuer(String username) {
        //System.out.println("reached getIssuer");
        //System.out.println("Username = " + username);
        ArrayList<String> info = (ArrayList<String>) database.get(username);
        //System.out.println("info received");
        
        //.out.println(info.toString());
        //System.out.println("printed?");
        //System.out.println(info.get(1));
        return info.get(1);
    }
    @Override
    public String getSubject(String username) {
        ArrayList<String> info = (ArrayList<String>) database.get(username);
        return info.get(3);
    }
    @Override
    public String getJWTId(String username) {
        ArrayList<String> info = (ArrayList<String>) database.get(username);
        return info.get(4);
    }
}
