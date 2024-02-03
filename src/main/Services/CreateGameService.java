package Services;
import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import Models.AuthToken;
import Models.Game;
import Requests.CreateGameRequest;
import Results.CreateGameResult;
import chess.ChessGame;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;

import java.sql.SQLException;

/**
 * Class that determines if creating game is possible.
 */
public class CreateGameService {
    /**
     * Head function that determines if creating game is valid.
     * @param r request to create game.
     * @return Returns Successful or unsuccessful result.
     */
    public CreateGameResult createGame(CreateGameRequest r) {
        Game newGame = new Game();
        GameDAO gameDAO = new GameDAO();
        AuthToken authToken = new AuthToken();
        ChessGame game = new ChessGameImpl();
        CreateGameResult result = new CreateGameResult();

        authToken.setAuthToken(r.getAuthToken());

        newGame.setGameName(r.getGameName());
        newGame.setGame(game);
        newGame.generateGameID();
        newGame.setGame(game);

        try {
            //Validate that given authToken is in DB
            AuthDAO.findAuthToken(r.getAuthToken()).getAuthToken();
            gameDAO.insertGame(newGame);
            result.setGameID(newGame.getGameID());

            return result;
        } catch (DataAccessException e) {
            result.setMessage(e.getMessage());
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}