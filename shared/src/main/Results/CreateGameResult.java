package Results;
/**
 * CreateGame result object that will be passed back to user after determining if creating a game is possible.
 */
public class CreateGameResult {
    /**
     * Game of ID of new game successfully created.
     */
    public String gameID;
    /**
     * Failure response message.
     */
    public String message;

    public void setGameID(String gameID) {this.gameID = gameID;}

    public String getGameID() {
        return gameID;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}