package Requests;
/**
 * Logout request object that will be sent to services to determine if logging out is possible.
 */
public class LogoutRequest {
    /**
     * AuthToken string of the user attempt to log out
     */
    private String authToken;

    public LogoutRequest(String authToken) {this.authToken = authToken;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}
}