package Server.websocket;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.Game;
import chess.ChessMoveImpl;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;


@WebSocket
public class WebSocketHandler {
    public ConnectionManager getConnections() {
        return connections;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    private final ConnectionManager connections = new ConnectionManager();
    private Game game = new Game();
    private String teamColor;
    String gameID;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinGame(action.getAuthString(), action.getGameID(), action.getPlayerColor(), session);
            case JOIN_OBSERVER -> observeGame(action.getAuthString(), action.getGameID(), session);
            case MAKE_MOVE -> makeMove(action.getAuthString(), action.getGameID(), session);

        }
    }

    private void makeMove(String authString, String gameID, Session session) {
        ChessMoveImpl move;
        //try {

        //}
    }

    private void observeGame(String authString, String gameID, Session session) throws IOException, DataAccessException, SQLException{
        GameDAO gameDAO = new GameDAO();
        Gson gson = new Gson();
        Game game;
        try {
            game = gameDAO.findGame(gameID);
        } catch(DataAccessException e) {
            var errorMessage = new ServerMessage(ERROR);
            errorMessage.setMessage("Bad GameID");
            errorMessage.setErrorMessage("Bad GameID");
            session.getRemote().sendString(gson.toJson(errorMessage));
            return;
        }
        connections.add(authString, session);
        AuthToken authToken = new AuthToken();
        try {
            authToken = AuthDAO.findAuthToken(authString);

        } catch (DataAccessException e) {
            var errorMessage = new ServerMessage(ERROR);
            System.out.println("Error");
            errorMessage.setMessage("Bad authToken");
            errorMessage.setErrorMessage("Bad authToken");
            session.getRemote().sendString(gson.toJson(errorMessage));
            return;
        }

        var message = String.format("%s is now viewing", authToken.getUsername());
        var notification = new ServerMessage(NOTIFICATION);//going to all other user
        var userMessage = new ServerMessage(LOAD_GAME);
        //going to user that requested, needs to have game with it
        if (gameDAO.findGame(gameID) == null) {
            var errorMessage = new ServerMessage(ERROR);
            errorMessage.setMessage("Bad Game ID");
            errorMessage.setErrorMessage("Bad Game ID");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } else if (Objects.equals(game.getGameID(), gameID)) {
            userMessage.setMessage("Game loaded");
            userMessage.setGame(game.getGame());
            session.getRemote().sendString(gson.toJson(userMessage));
            notification.setMessage(message);
            connections.broadcast(authToken.getAuthToken(), notification);//for other clients
        }  else {
            var errorMessage = new ServerMessage(ERROR);
            errorMessage.setMessage("User not added");
            errorMessage.setErrorMessage("User not added");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private void joinGame(String authToken1, String gameID, String color, Session session) throws IOException, DataAccessException, SQLException {
        GameDAO gameDAO = new GameDAO();
        Gson gson = new Gson();
        Game game;

        try {
            game = gameDAO.findGame(gameID);

        } catch(DataAccessException e) {
            var errorMessage = new ServerMessage(ERROR);
            System.out.println("Error");
            errorMessage.setMessage("Bad GameID");
            errorMessage.setErrorMessage("Bad GameID");
            session.getRemote().sendString(gson.toJson(errorMessage));
            return;
        }
        connections.add(authToken1, session);
        AuthToken authToken = new AuthToken();
        try {
            authToken = AuthDAO.findAuthToken(authToken1);

        } catch (DataAccessException e) {
            var errorMessage = new ServerMessage(ERROR);
            System.out.println("Error");
            errorMessage.setMessage("Bad authToken");
            errorMessage.setErrorMessage("Bad authToken");
            session.getRemote().sendString(gson.toJson(errorMessage));
            return;
        }

        var message = String.format("%s joined as %s", authToken.getUsername(), color);
        var notification = new ServerMessage(NOTIFICATION);//going to all other user
        var userMessage = new ServerMessage(LOAD_GAME);
        //going to user that requested, needs to have game with it
        if (gameDAO.findGame(gameID) == null) {
            var errorMessage = new ServerMessage(ERROR);
            errorMessage.setMessage("Bad Game ID");
            errorMessage.setErrorMessage("Bad Game ID");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } else if (Objects.equals(game.getGameID(), gameID) && Objects.equals(game.getWhiteUsername(), authToken.getUsername()) && Objects.equals(color, "WHITE")) {
            userMessage.setMessage("Game loaded");
            userMessage.setGame(game.getGame());
            session.getRemote().sendString(gson.toJson(userMessage));
            notification.setMessage(message);
            notification.setColor(color);
            connections.broadcast(authToken.getAuthToken(), notification);//for other clients
        } else if(Objects.equals(game.getGameID(), gameID) && Objects.equals(game.getBlackUsername(), authToken.getUsername()) && Objects.equals(color, "BLACK")) {
            userMessage.setMessage("Game loaded");
            userMessage.setGame(game.getGame());
            session.getRemote().sendString(gson.toJson(userMessage));
            notification.setMessage(message);
            notification.setColor(color);
            connections.broadcast(authToken.getAuthToken(), notification);//for other clients
        } else {
            var errorMessage = new ServerMessage(ERROR);
            System.out.println("Error");
            errorMessage.setMessage("User not added");
            errorMessage.setErrorMessage("User not added");
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
        //check that it was added, then return that gameboard
        //only send below if it was successful, send error if not
        //for person that requested it
    }

    @OnWebSocketClose
    public void close(Session session, int statusCode, String reason) {
        getConnections().remove(session);
    }
}
