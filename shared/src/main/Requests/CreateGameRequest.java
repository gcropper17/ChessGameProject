package Requests;
/**
 * CreateGame request object that will be sent to services to determine if creating a game is possible.
 */
public class CreateGameRequest {
    /**
     * Name of new game.
     */
    private final String gameName;
    /**
     * AuthToken of player creating game.
     */
    private String authToken;

    public CreateGameRequest(String gameName) {this.gameName = gameName;}

    public String getGameName() {return gameName;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}
}