package Services;
import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.Game;
import Models.User;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import dataAccess.DataAccessException;

import java.sql.SQLException;

/**
 * Class that determines if Joining game is possible.
 */
public class JoinGameService {
    /**
     * Head function that determines if joining a game is possible or not.
     * @param r Request to join game.
     * @return Returns successful or unsuccessful result.
     */
    public JoinGameResult joinGame(JoinGameRequest r) throws DataAccessException {
        GameDAO gameDAO = new GameDAO();
        UserDAO userDAO = new UserDAO();
        JoinGameResult result = new JoinGameResult();
        Game game;
        User user;
        AuthToken authToken;
        try {
            //Get authToken
            authToken = AuthDAO.findAuthToken(r.getAuthToken());

            //Find game and user from request
            game = gameDAO.findGame(r.getGameID());
            String userName = authToken.getUsername();
            user = userDAO.findUser(userName);

            //Check if player is wanting to view a game
            if (r.getPlayerColor() == null) {
                GameDAO.addViewer(user);
            } else {
                gameDAO.claimSpot(user, game, r.getPlayerColor());
            }

            return result;
        } catch (DataAccessException e) {
            result.setMessage(e.getMessage());
            return result;
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}