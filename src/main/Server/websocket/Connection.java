package Server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String playerName;
    public Session session;

    public Connection(String visitorName, Session session) {
        this.playerName = visitorName;
        this.session = session;
    }

    public void send(String msg) throws IOException, IOException {
        Gson gson = new Gson();
        session.getRemote().sendString(msg);
    }
}
