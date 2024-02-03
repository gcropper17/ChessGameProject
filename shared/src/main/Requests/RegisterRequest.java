package Requests;
/**
 * Register request object that will be sent to services to determine if registering a new user is possible.
 */
public class RegisterRequest {
    /**
     * Username of new player.
     */
    String username;
    /**
     * Password of new player.
     */
    String password;
    /**
     * Email that will be used for new player.
     */
    String email;

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}