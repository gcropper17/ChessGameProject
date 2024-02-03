package passoffTests.Services;
import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.Game;
import Models.User;
import Requests.*;
import Results.*;
import Services.*;
import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
public class ServiceTests {

    @Test
    @Order(1)
    @DisplayName("Positive clear test")
    public void ClearAppPositive() throws DataAccessException, SQLException {
        GameDAO gameDAO = new GameDAO();
        ClearAppService clearAppService = new ClearAppService();

        Game newGame = createGame("Test Game");
        gameDAO.insertGame(newGame);
        clearAppService.clear();

        assertEquals(0,gameDAO.findAllGames().size(), "Game found in DB");
    }

    @Test
    @Order(2)
    @DisplayName("Create game positive test")
    public void positiveCreateGame() throws DataAccessException, SQLException {
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        CreateGameService createGameService = new CreateGameService();

        clear();
        //AuthToken needed to add game
        AuthToken authToken = createAuthToken("Bob");
        authDAO.insertAuthToken(authToken);

        //Insert game with unique game name and valid authToken
        CreateGameRequest createGameRequest = new CreateGameRequest("Test Game");
        createGameRequest.setAuthToken(authToken.getAuthToken());
        createGameService.createGame(createGameRequest);

        assertEquals(1, gameDAO.findAllGames().size(), "Number of games not the same");
    }

    @Test
    @Order(3)
    @DisplayName("Create game negative test")
    public void negativeCreateGame() throws DataAccessException, SQLException {
        GameDAO gameDAO = new GameDAO();
        CreateGameService createGameService = new CreateGameService();
        String comparisonString;

        clear();



        CreateGameRequest createGameRequest = new CreateGameRequest("Test Game");

        //Create another game with non-unique name
        CreateGameResult createGameResult;
        createGameResult = createGameService.createGame(createGameRequest);
        comparisonString = createGameResult.getMessage();

        assertEquals("Error: unauthorized", comparisonString, "Error message returned not the same");
    }

    @Test
    @Order(4)
    @DisplayName("Join game positive test")
    public void positiveJoinGame () throws DataAccessException, SQLException {
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        JoinGameService joinGameService = new JoinGameService();
        JoinGameRequest joinGameRequest = new JoinGameRequest();

        Game joinableJoinedGame;
        clear();

        //Add needed User, AuthToken, and Game to DB
        Game newGame = createGame("Test Game");
        User user = createUser("Bob", "pw", "Bob@email.com");
        userDAO.insertUser(user);
        AuthToken authToken = createAuthToken(user.getUsername());
        authDAO.insertAuthToken(authToken);
        gameDAO.insertGame(newGame);
        String whiteUsername = "";

        //Set up request
        joinGameRequest.setAuthToken(authToken.getAuthToken());
        joinGameRequest.setGameID(newGame.getGameID());
        joinGameRequest.setPlayerColor("WHITE");

        //Run Test
        joinGameService.joinGame(joinGameRequest);
        joinableJoinedGame = gameDAO.findGame(joinGameRequest.getGameID());
        whiteUsername = joinableJoinedGame.getWhiteUsername();

        assertEquals("Bob", whiteUsername, "Request returned incorrect user");
    }

    @Test
    @Order(5)
    @DisplayName("Join game negative test")
    public void negativeJoinGame() throws DataAccessException, SQLException {
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        JoinGameService joinGameService = new JoinGameService();
        JoinGameRequest joinGameRequest = new JoinGameRequest();
        JoinGameResult joinGameResult;
        String resultMessage;

        clear();

        //Add needed User, AuthToken, and Game to DB
        Game newGame = createGame("Test Game");
        User user = createUser("Bob", "pw", "Bob@email.com");
        userDAO.insertUser(user);
        AuthToken authToken = createAuthToken(user.getUsername());
        authDAO.insertAuthToken(authToken);
        gameDAO.insertGame(newGame);

        //Claim White spot in game
        gameDAO.claimSpot(user, newGame, "WHITE");

        //Set up request
        joinGameRequest.setAuthToken(authToken.getAuthToken());
        joinGameRequest.setGameID(newGame.getGameID());
        joinGameRequest.setPlayerColor("WHITE");

        //Run Test with white spot already taken
        joinGameResult = joinGameService.joinGame(joinGameRequest);
        resultMessage = joinGameResult.getMessage();

        assertEquals("Error: already taken", resultMessage, "Request returned incorrect message");
    }

    @Test
    @Order(6)
    @DisplayName("List games positive test")
    public void positiveListGames() throws DataAccessException, SQLException {
        Collection<Game> expectedList = new ArrayList<>();
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        ListGamesRequest listGamesRequest = new ListGamesRequest(null);
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult listGamesResult;

        clear();
        //Create user, authToken, and games for DB
        Game game1 = createGame("Test Game");
        Game game2 = createGame("Test Game 2");
        gameDAO.insertGame(game1);
        gameDAO.insertGame(game2);
        User user = createUser("Bob", "pw", "Bob@email.com");
        AuthToken authToken = createAuthToken(user.getUsername());
        userDAO.insertUser(user);
        authDAO.insertAuthToken(authToken);

        expectedList.add(game1);
        expectedList.add(game2);

        listGamesRequest.setAuthToken(authToken.getAuthToken());

        //Run test
        listGamesResult = listGamesService.listGames(listGamesRequest);

        assertEquals(expectedList, listGamesResult.getGames(), "List returned not same as expected");
    }

    @Test
    @Order(7)
    @DisplayName("List games negative test")
    public void negativeListGames() throws DataAccessException, SQLException {
        Collection<Game> expectedList = new ArrayList<>();
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();
        UserDAO userDAO = new UserDAO();
        ListGamesRequest listGamesRequest = new ListGamesRequest(null);
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult listGamesResult;

        clear();

        //Create user, authToken, and games for DB
        Game game1 = createGame("Test Game");
        Game game2 = createGame("Test Game 2");
        gameDAO.insertGame(game1);
        //gameDAO.insertGame(game2);
        User user = createUser("Bob", "pw", "Bob@email.com");
        AuthToken authToken = createAuthToken(user.getUsername());
        userDAO.insertUser(user);
        authDAO.insertAuthToken(authToken);

        expectedList.add(game1);
        expectedList.add(game2);

        listGamesRequest.setAuthToken(authToken.getAuthToken());

        //Run test
        listGamesResult = listGamesService.listGames(listGamesRequest);

        assertNotEquals(expectedList, listGamesResult.getGames(), "List returned are the same");
    }

    @Test
    @Order(8)
    @DisplayName("Positive login test")
    public void positiveLogin() throws DataAccessException {
        UserDAO userDAO = new UserDAO();
        LoginService loginService = new LoginService();
        LoginRequest loginRequest = new LoginRequest();
        LoginResult loginResult;

        clear();

        //Create user and authToken in DB
        User user = createUser("Bob", "pw", "Bob@email.com");
        userDAO.insertUser(user);

        //Set up login request
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        //Run Test
        loginResult = loginService.login(loginRequest);

        assertNotNull(loginResult.getAuthToken(), "Result returned null");
    }

    @Test
    @Order(9)
    @DisplayName("Negative login test")
    public void negativeLogin() throws DataAccessException {
        UserDAO userDAO = new UserDAO();
        LoginService loginService = new LoginService();
        LoginRequest loginRequest = new LoginRequest();
        LoginResult loginResult;

        clear();

        //Create user in DB
        User user = createUser("Bob", "pw", "Bob@email.com");
        userDAO.insertUser(user);

        //Set up login request
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword("pq");

        //Run Test with incorrect password
        loginResult = loginService.login(loginRequest);

        assertEquals("Error: unauthorized", loginResult.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("Positive logout test")
    public void positiveLogout() throws DataAccessException, SQLException {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        LogoutService logoutService = new LogoutService();
        LogoutRequest logoutRequest = new LogoutRequest(null);
        LogoutResult loginResult;

        clear();

        //Create user and authToken in DB
        User user = createUser("Bob", "pw", "Bob@email.com");
        AuthToken authToken = createAuthToken(user.getUsername());
        userDAO.insertUser(user);
        authDAO.insertAuthToken(authToken);

        //Set up login request
        logoutRequest.setAuthToken(authToken.getAuthToken());

        //Run Test
        loginResult = logoutService.logout(logoutRequest);

        assertNull(loginResult.getMessage(), "Result returned not null");
    }

    @Test
    @Order(11)
    @DisplayName("Negative logout test")
    public void negativeLogout() throws DataAccessException {
        UserDAO userDAO = new UserDAO();
        LogoutService logoutService = new LogoutService();
        LogoutRequest logoutRequest = new LogoutRequest(null);
        LogoutResult loginResult;

        clear();

        //Create user and authToken in DB
        User user = createUser("Bob", "pw", "Bob@email.com");
        AuthToken authToken = createAuthToken(user.getUsername());
        userDAO.insertUser(user);

        //Set up logout request
        logoutRequest.setAuthToken(authToken.getAuthToken());

        //Run Test without any valid authTokens in DB
        loginResult = logoutService.logout(logoutRequest);

        assertEquals("Error: unauthorized", loginResult.getMessage());
    }

    @Test
    @Order(12)
    @DisplayName("Positive register test")
    public void positiveRegister() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterResult registerResult;

        clear();

        User user = createUser("Bob", "pw", "Bob@email.com");

        //Set up request
        registerRequest.setUsername(user.getUsername());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setEmail(user.getEmail());

        //Run test
        registerResult = registerService.register(registerRequest);

        assertEquals(user.getUsername(), registerResult.getUsername(), "Request return not the same");
    }

    @Test
    @Order(13)
    @DisplayName("Negative register test")
    public void negativeRegister() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest();
        RegisterResult registerResult;

        clear();

        User user = createUser("Bob", "pw", "Bob@email.com");

        //Set up request
        registerRequest.setUsername(user.getUsername());
        registerRequest.setPassword(user.getPassword());

        //Run test without email
        registerResult = registerService.register(registerRequest);

        assertEquals("Error: bad request", registerResult.getMessage(), "Request returned as not a bad request");
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
