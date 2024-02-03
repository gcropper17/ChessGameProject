package passoffTests.Services;

import DataAccess.AuthDAO;
import DataAccess.Database;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.Game;
import Models.User;
import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Requests.ListGamesRequest;
import Results.CreateGameResult;
import Results.JoinGameResult;
import Results.ListGamesResult;
import Results.LoginResult;
import Services.ClearAppService;
import Services.CreateGameService;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ui.ServerFacade;
import ui.exception.ResponseException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class phase5tests {
    @Test
    @Order(1)
    @DisplayName("Create game positive")
    void createNewGamePositive() throws DataAccessException, ResponseException, SQLException {
        // Mock data for the new game
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();
        Game newGame = createGame("game");
        User u = createUser("Bob", "pw", "email");
        AuthToken authToken = createAuthToken(u.getUsername());
        authDAO.insertAuthToken(authToken);

        // Test server facade create game
        ServerFacade server = new ServerFacade("http://localhost:8080");
        CreateGameResult result = server.createNewGame(newGame, authToken.getAuthToken());
        assertEquals("game", gameDAO.findGame(result.getGameID()).getGameName(), "ID's do not match");
        clear();
    }

    @Test
    @Order(2)
    @DisplayName("Create game negative")

    void createNewGameNegative() throws SQLException, DataAccessException, ResponseException {
        // Mock data for the new game
        AuthDAO authDAO = new AuthDAO();
        Game newGame = createGame("game");
        User u = createUser("Bob", "pw", "email");
        AuthToken authToken = createAuthToken(u.getUsername());

        // Test server facade create game, should return unauthorized
        try {
            ServerFacade server = new ServerFacade("http://localhost:8080");
            server.createNewGame(newGame, authToken.getAuthToken());
            fail();
        } catch (ResponseException e) {
            assertEquals("failure: 401", e.getMessage(), "Game added when it shouldn't");
        }
        clear();
    }

    @Test
    @Order(3)
    @DisplayName("Register positive")

    public void registerPositive() throws ResponseException, DataAccessException {
        clear();
        UserDAO userDAO = new UserDAO();
        User user = createUser("Bob", "pw", "email");
        ServerFacade server = new ServerFacade("http://localhost:8080");
        server.registerUser(user);
        assertEquals(user.getUsername(), userDAO.findUser(user.getUsername()).getUsername(), "User not the same");
        clear();
    }
    @Test
    @Order(4)
    @DisplayName("Register Negative")
    public void registerNegative() throws ResponseException, DataAccessException {
        User user = createUser("Bob", "pw", "email");
        ServerFacade server = new ServerFacade("http://localhost:8080");
        try {
            server.registerUser(user);
            server.registerUser(user);
            fail();
        } catch (ResponseException e) {
            assertEquals("failure: 403", e.getMessage(), "User added twice");
        }
        clear();
    }

    @Test
    @Order(5)
    @DisplayName("Login positive")
    public void loginPositive() throws ResponseException, DataAccessException {
        UserDAO userDAO = new UserDAO();
        User user = createUser("Bob", "pw", "email");
        userDAO.insertUser(user);
        ServerFacade server = new ServerFacade("http://localhost:8080");
        LoginResult result = server.loginUser(user);
        //assertEquals(result.getAuthToken(), AuthDAO.findAuthToken(result.getAuthToken()).getAuthToken(), "AuthToken not found");
        clear();
    }

    @Test
    @Order(6)
    @DisplayName("Login Negative")
    public void loginNegative() throws DataAccessException {
        try {
            User user = createUser("Bob", "pw", "email");
            ServerFacade server = new ServerFacade("http://localhost:8080");
            server.loginUser(user);
            fail();
        } catch (ResponseException e) {
            assertEquals("failure: 401", e.getMessage(), "Error messages do not match");
        }
        clear();
    }
    
    @Test
    @Order(7)
    @DisplayName("List games positive")
    public void listGamesPositive() throws SQLException, DataAccessException, ResponseException {
        clear();
        GameDAO gameDAO = new GameDAO();
        Game newGame = createGame("game");
        Game newGame1 = createGame("game1");
        User user = createUser("Bob", "pw", "email");
        ServerFacade server = new ServerFacade("http://localhost:8080");
        server.registerUser(user);
        LoginResult result = server.loginUser(user);
        server.createNewGame(newGame, result.getAuthToken());
        server.createNewGame(newGame1, result.getAuthToken());
        assertEquals(2, gameDAO.findAllGames().size(), "Not same size");
        clear();
    }

    @Test
    @Order(8)
    @DisplayName("List games negative")
    public void listGamesNegative() throws DataAccessException {
        clear();
        try {
            Game newGame = createGame("game");
            Game newGame1 = createGame("game1");
            User user = createUser("Bob", "pw", "email");
            ServerFacade server = new ServerFacade("http://localhost:8080");
            server.registerUser(user);
            LoginResult result = server.loginUser(user);
            server.createNewGame(newGame, result.getAuthToken());
            server.createNewGame(newGame1, result.getAuthToken());
            AuthDAO.clear();
            ListGamesRequest request = new ListGamesRequest(result.getAuthToken());
            server.listGames(request);
            fail();
        } catch (ResponseException e) {
            assertEquals("failure: 401", e.getMessage(), "Messages not same");
        }
        clear();
    }

    @Test
    @Order(9)
    @DisplayName("Join game positive")
    public void joinGamePositive() throws ResponseException, DataAccessException {
        clear();
        Game newGame = createGame("game");
        User user = createUser("Bob", "pw", "email");
        ServerFacade server = new ServerFacade("http://localhost:8080");
        server.registerUser(user);
        LoginResult loginResult = server.loginUser(user);
        CreateGameResult createGameResult = server.createNewGame(newGame, loginResult.getAuthToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest();
        joinGameRequest.setAuthToken(loginResult.getAuthToken());
        joinGameRequest.setGameID(createGameResult.getGameID());
        joinGameRequest.setPlayerColor("Black");
        JoinGameResult result = server.join(joinGameRequest);
        assertEquals(null, result.getMessage(), "game not joined");
    }

    @Test
    @Order(10)
    @DisplayName("Join game negative")
    public void joinGameNegative() throws ResponseException, DataAccessException {
        clear();
        JoinGameResult result = null;
        try {
            Game newGame = createGame("game");
            User user = createUser("Bob", "pw", "email");
            ServerFacade server = new ServerFacade("http://localhost:8080");
            server.registerUser(user);
            LoginResult loginResult = server.loginUser(user);
            CreateGameResult createGameResult = server.createNewGame(newGame, loginResult.getAuthToken());
            JoinGameRequest joinGameRequest = new JoinGameRequest();
            joinGameRequest.setAuthToken(loginResult.getAuthToken());
            joinGameRequest.setGameID(createGameResult.getGameID());
            joinGameRequest.setPlayerColor("Black");
            server.join(joinGameRequest);
            server.join(joinGameRequest);
            fail();
        } catch (ResponseException e) {
            assertEquals("failure: 403", e.getMessage(), "Game incorrectly added");
        }
        clear();

    }



    /*----------Helper functions for tests----------*/
    private User createUser(String name, String password, String email) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }

    private Game createGame(String gameName) {
        Game game = new Game();
        game.setGameName(gameName);
        game.generateGameID();
        game.setGame(new ChessGameImpl());
        return game;
    }

    private AuthToken createAuthToken (String userName) {
        AuthToken authToken = new AuthToken();
        authToken.setAndGenerateAuthToken();
        authToken.setUsername(userName);
        return authToken;
    }

    private void clear() throws DataAccessException {
        ClearAppService clearAppService = new ClearAppService();
        clearAppService.clear();
    }
}
