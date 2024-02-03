package Requests;
/**
 * JoinGame request object that will be sent to services to determine if joining a game is possible.
 */
public class JoinGameRequest {
    /**
     * Color that will be assigned to player
     */
    public String playerColor;
    /**
     * ID of game being joined
     */
    public String gameID;
    /**
     * AuthToken of player to make sure player is authorized.
     */
    public String authToken;

    public String getPlayerColor() {return playerColor;}

    public String getGameID() {return gameID;}

    public String getAuthToken() {return authToken;}

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public void setPlayerColor(String playerColor) {this.playerColor = playerColor;}

    public void setGameID(String gameID) {this.gameID = gameID;}
}