package Requests;
/**
 * Login request object that will be sent to services to determine if logging in is possible.
 */
public class LoginRequest {
    /**
     * String type for the username attempting to log in
     */
    private String username;
    /**
     * String type for the password of the username
     */
    private String password;

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}