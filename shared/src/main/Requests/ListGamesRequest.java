package Requests;
/**
 * ListGames request object that will be sent to services to determine if Listing all games is possible.
 */
public class ListGamesRequest {
    /**
     * AuthToken to confirm that user is authorized.
     */
    String authToken;
    /**
     * List Game request object that will be sent to the services.
     * @param authToken Checks to make sure if the authToken is valid.
     */
    public ListGamesRequest(String authToken) {this.authToken = authToken;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}
}