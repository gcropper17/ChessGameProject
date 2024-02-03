package Server;

import Handlers.*;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import Server.websocket.WebSocketHandler;
import Services.JoinGameService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;


import java.util.*;

public class Server {
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();
    public static void main(String[] args) {new Server().run();}

    private void run() {
        Spark.port(8080);
        Spark.webSocket("/connect", webSocketHandler);
        Spark.externalStaticFileLocation("web");

        //Register endpoints
        Spark.post("/user", new RegisterHandler());

        //Login Endpoints
        Spark.post("/session", new LoginHandler());

        //Logout Endpoint
        Spark.delete("/session", new LogoutHandler());

        //Clear
        Spark.delete("/db", new ClearAppHandler());

        //Create new game
        Spark.post("/game", new CreateGameHandler());

        //Join Games
        Spark.put("/game", new JoinGameHandler());

        //List Games
        Spark.get("/game", new ListGamesHandler());

        //Error
        Spark.get("/error", this::throwError);

        Spark.exception(Exception.class, this::errorHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(msg), req, res);
        });
    }


    private Object throwError(Request req, Response res) {throw new RuntimeException("Server.Server on fire");}

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }
}