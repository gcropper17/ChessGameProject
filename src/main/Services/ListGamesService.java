package Services;
import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Models.Game;
import Requests.ListGamesRequest;
import Results.ListGamesResult;
import dataAccess.DataAccessException;

import java.sql.SQLException;
import java.util.Collection;
/**
 * Class that determines if listing games is possible.
 */
public class ListGamesService {
    /**
     * Head method to determine if listing games is possible.
     * @param r Request to list games.
     * @return Returns successful or unsuccessful result.
     */
    public ListGamesResult listGames(ListGamesRequest r) {
        ListGamesResult result = new ListGamesResult();
        GameDAO gameDAO = new GameDAO();
        Collection<Game> games;

        try {
            //Validate that given authToken is in DB
            AuthDAO.findAuthToken(r.getAuthToken()).getAuthToken();
            games = gameDAO.findAllGames();
            result.setGames(games);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            return result;
        }

        return result;
    }
}