package ui;

import Models.Game;
import Models.User;
import Requests.JoinGameRequest;
import Requests.ListGamesRequest;
import Requests.LogoutRequest;
import Requests.RegisterRequest;
import Results.*;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ui.exception.ResponseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerUser(User user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, RegisterResult.class, null);
    }

    public LoginResult loginUser(User user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, LoginResult.class, null);
    }

    public CreateGameResult createNewGame(Game game, String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game, CreateGameResult.class, authToken);
    }

    public ListGamesResult listGames (ListGamesRequest request) throws ResponseException {
        String authToken = request.getAuthToken();
        var path = "/game";
        var response = this.makeRequest("GET", path, null, ListGamesResult.class, authToken);;
        return response;
    }

    public JoinGameResult join (JoinGameRequest request) throws ResponseException {
        String authToken = request.getAuthToken();
        var path = "/game";
        var response = this.makeRequest("PUT", path, request, JoinGameResult.class, authToken);
        return response;
    }

    public LogoutResult logoutUser(LogoutRequest request) throws ResponseException {
        String authToken = request.getAuthToken();
        var path = "/session";
        var response = this.makeRequest("DELETE", path, request, LogoutResult.class, authToken);
        return response;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass == Game.class) {
                    Gson gson = new GsonBuilder().registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameAdapter()).create();
                    response = gson.fromJson(reader, responseClass);
                }
                else if (responseClass != null) {
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
