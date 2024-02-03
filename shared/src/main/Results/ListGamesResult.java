package Results;
import Models.Game;
import java.util.Collection;
/**
 * ListGames result object that will be passed back to user after determining if listing all games is possible.
 */
public class ListGamesResult {
    /**
     * message sent if request fails.
     */
    String message;
    /**
     * List of games that will be sent as part of success response.
     */
    Collection<Game> games;

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public Collection<Game> getGames() {return games;}

    public void setGames(Collection<Game> games) {this.games = games;}


}