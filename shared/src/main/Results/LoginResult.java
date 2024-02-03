package Results;
/**
 * LoginResult object that will be passed back to user after determining if logging in is possible.
 */
public class LoginResult {
    /**
     * Username that will be logged
     */
    private String username;
    /**
     * AuthToken that the username is connected to.
     */
    private String authToken;
    /**
     * Message sent if the request failed
     */
    private String message;

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}