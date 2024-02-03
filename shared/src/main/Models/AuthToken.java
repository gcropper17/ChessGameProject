package Models;
import java.util.UUID;
/**
 * Class contains the main properties for AuthToken, the authToken and the username.
 */
public class AuthToken {
    /**
     * authToken that will be created for user.
     */
    private String authToken;
    /**
     * Username that will have a authToken made.
     */
    private String username;

    public void setAndGenerateAuthToken() {authToken = UUID.randomUUID().toString();}

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public String getAuthToken() {return authToken;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
}