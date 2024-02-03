package Server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        connections.put(visitorName, connection);
    }

    public void remove(Session session) {
        String visitorName = null;
        for (var item : connections.keySet()) {
            if (connections.get(item).session == session) {
                visitorName = item;
                break;
            }
        }
        if (visitorName != null) {
            connections.remove(visitorName);

        }
    }

    public void broadcast(String excludePlayerName, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        Gson gson = new Gson();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.playerName.equals(excludePlayerName)) {
                    c.send(gson.toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerName);
        }
    }
}
