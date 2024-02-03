package Results;
/**
 * Register result object that will be passed back to user after determining if registering is possible.
 */
public class RegisterResult {
    /**
     * username of new user will be message sent if request is successful.
     */
    private String username;
    /**
     * authToken of new user will be message sent if request is successful.
     */
    private String authToken;
    /**
     * Message sent if request fails.
     */
    private String message;

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}