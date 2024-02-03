package ui.websocket;

import chess.ChessGame;
import chess.ChessMoveImpl;
import com.google.gson.Gson;
import ui.exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;

import com.google.gson.Gson;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    System.out.println(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinGame(String authToken, String gameID, String color) throws ResponseException, IOException {
        try {
            var userGameCommand = new UserGameCommand(authToken, gameID, color);
            userGameCommand.setCommandType(UserGameCommand.CommandType.JOIN_PLAYER);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

    public void makeMove(String authToken, String gameID, ChessMoveImpl move, String color) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(authToken, gameID, color);
            userGameCommand.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));

        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
